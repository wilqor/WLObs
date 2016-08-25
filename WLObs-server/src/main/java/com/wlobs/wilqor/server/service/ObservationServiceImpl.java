/*
 * Copyright 2016 wilqor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wlobs.wilqor.server.service;

import com.wlobs.wilqor.server.persistence.model.Observation;
import com.wlobs.wilqor.server.persistence.model.Species;
import com.wlobs.wilqor.server.persistence.repository.ObservationRepository;
import com.wlobs.wilqor.server.rest.model.ExistingObservationDto;
import com.wlobs.wilqor.server.rest.model.NewObservationDto;
import com.wlobs.wilqor.server.rest.model.ObservationRestrictionDto;
import com.wlobs.wilqor.server.service.exceptions.ObservationAlreadyExistsException;
import com.wlobs.wilqor.server.service.exceptions.ObservationNotFoundException;
import com.wlobs.wilqor.server.service.exceptions.ObservationRestrictedException;
import com.wlobs.wilqor.server.service.exceptions.ObservedSpeciesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wilqor
 */
@Service
public class ObservationServiceImpl implements ObservationService {
    private final ObservationRepository observationRepository;
    private final SpeciesService speciesService;
    private final GeoHashService geoHashService;

    @Autowired
    public ObservationServiceImpl(ObservationRepository observationRepository, SpeciesService speciesService, GeoHashService geoHashService) {
        this.observationRepository = observationRepository;
        this.speciesService = speciesService;
        this.geoHashService = geoHashService;
    }

    @Override
    public void addObservation(String login, NewObservationDto observationDto) {
        Optional<Species> species = speciesService.findSpeciesForStub(observationDto.getSpeciesStub());
        species.orElseThrow(() -> new ObservedSpeciesNotFoundException(observationDto.getSpeciesStub()));
        String geoHash = geoHashService.getGeoHashForLocation(observationDto.getLatitude(), observationDto.getLongitude());
        Optional<Observation> foundObservation = observationRepository.findByAuthorAndDateTimestampAndGeoHashAndSpeciesStub(
                login,
                observationDto.getDateUtcTimestamp(),
                geoHash,
                observationDto.getSpeciesStub()
        );
        foundObservation.ifPresent(o -> {
            throw new ObservationAlreadyExistsException(observationDto);
        });
        observationRepository.save(
                new Observation.Builder()
                        .author(login)
                        .dateTimestamp(observationDto.getDateUtcTimestamp())
                        .geoHash(geoHash)
                        .location(new GeoJsonPoint(
                                observationDto.getLongitude(),
                                observationDto.getLatitude()
                        ))
                        .speciesStub(observationDto.getSpeciesStub())
                        .restricted(observationDto.isRestricted())
                        .votesCount(0)
                        .build()
        );
    }

    @Override
    public void updateObservationRestriction(String login, String id, ObservationRestrictionDto observationRestrictionDto) {
        Optional<Observation> found = observationRepository.findByAuthorAndId(login, id);
        Observation observation = found.orElseThrow(() -> new ObservationNotFoundException(id));
        observation.setRestricted(observationRestrictionDto.isRestricted());
        observationRepository.save(observation);
    }

    @Override
    public void removeObservation(String login, String id) {
        Optional<Observation> found = observationRepository.findByAuthorAndId(login, id);
        Observation observation = found.orElseThrow(() -> new ObservationNotFoundException(id));
        observationRepository.delete(observation);
    }

    @Override
    public List<ExistingObservationDto> getUserObservations(String login) {
        return observationRepository.findByAuthor(login)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExistingObservationDto getUserObservation(String principalLogin, String login, String observationId) {
        Optional<Observation> found = observationRepository.findByAuthorAndId(login, observationId);
        Observation observation = found.orElseThrow(() -> new ObservationNotFoundException(observationId));
        if (!observation.getAuthor().equals(principalLogin) && observation.isRestricted()) {
            throw new ObservationRestrictedException(observationId);
        }
        return convertToDto(observation);
    }

    @Override
    public Optional<Observation> getObservation(String observationId) {
        return observationRepository.findById(observationId);
    }

    private ExistingObservationDto convertToDto(Observation observation) {
        return new ExistingObservationDto.Builder()
                .id(observation.getId())
                .author(observation.getAuthor())
                .votesCount(observation.getVotesCount())
                .dateUtcTimestamp(observation.getDateTimestamp())
                .speciesStub(observation.getSpeciesStub())
                .restricted(observation.isRestricted())
                .longitude(observation.getLocation().getX())
                .latitude(observation.getLocation().getY())
                .build();
    }
}
