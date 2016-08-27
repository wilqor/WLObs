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

import ch.hsr.geohash.WGS84Point;
import com.wlobs.wilqor.server.config.GeoHashConstants;
import com.wlobs.wilqor.server.persistence.model.AggregatedObservation;
import com.wlobs.wilqor.server.persistence.model.Observation;
import com.wlobs.wilqor.server.persistence.model.Species;
import com.wlobs.wilqor.server.persistence.repository.ObservationRepository;
import com.wlobs.wilqor.server.rest.model.*;
import com.wlobs.wilqor.server.service.exceptions.ObservationAlreadyExistsException;
import com.wlobs.wilqor.server.service.exceptions.ObservationNotFoundException;
import com.wlobs.wilqor.server.service.exceptions.ObservationRestrictedException;
import com.wlobs.wilqor.server.service.exceptions.ObservedSpeciesNotFoundException;
import com.wlobs.wilqor.server.service.model.AggregationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ExistingObservationDto removeAndReturnObservation(String login, String id) {
        Optional<Observation> found = observationRepository.findByAuthorAndId(login, id);
        Observation observation = found.orElseThrow(() -> new ObservationNotFoundException(id));
        ExistingObservationDto asDto = convertToExistingObservationDto(observation);
        observationRepository.delete(observation);
        return asDto;
    }

    @Override
    public List<ExistingObservationDto> getUserObservations(String login) {
        return observationRepository.findByAuthor(login)
                .stream()
                .map(this::convertToExistingObservationDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExistingObservationDto getUserObservation(String principalLogin, String login, String observationId) {
        Optional<Observation> found = observationRepository.findByAuthorAndId(login, observationId);
        Observation observation = found.orElseThrow(() -> new ObservationNotFoundException(observationId));
        if (!observation.getAuthor().equals(principalLogin) && observation.isRestricted()) {
            throw new ObservationRestrictedException(observationId);
        }
        return convertToExistingObservationDto(observation);
    }

    @Override
    public Optional<Observation> getObservation(String observationId) {
        return observationRepository.findById(observationId);
    }

    @Override
    public void incrementObservationVotesCount(String observationId) {
        observationRepository.incrementObservationVotesCount(observationId);
    }

    @Override
    public void decrementObservationVotesCount(String observationId) {
        observationRepository.decrementObservationVotesCount(observationId);
    }

    @Override
    public AggregationResponseDto getAggregatedObservations(AggregationRequestDto aggregationRequestDto) {
        AggregationRequest request = new AggregationRequest.Builder()
                .area(aggregationRequestDto.getArea())
                .timeRange(aggregationRequestDto.getTimeRange())
                .speciesClass(aggregationRequestDto.getSpeciesClass())
                .speciesLatinName(aggregationRequestDto.getSpeciesLatinName())
                .build();
        List<ExistingObservationDto> existingObservationDtoList = new ArrayList<>();
        List<AggregatedObservationDto> aggregatedObservationDtoList = new ArrayList<>();
        int commonPrefixLength = geoHashService.getCommonPrefixLength(request.getArea());
        if (commonPrefixLength >= GeoHashConstants.GEO_HASH_AGGREGATION_THRESHOLD) {
            existingObservationDtoList = observationRepository.getRawObservations(request)
                    .stream()
                    .map(this::convertToExistingObservationDto)
                    .collect(Collectors.toList());
        } else {
            aggregatedObservationDtoList = observationRepository.getAggregatedObservations(request, commonPrefixLength)
                    .stream()
                    .map(this::convertToAggregatedObservationDto)
                    .collect(Collectors.toList());
        }
        return new AggregationResponseDto(existingObservationDtoList, aggregatedObservationDtoList);
    }

    @Override
    public RecordsPageDto<FlatObservationDto> getObservationsPage(int pageNumber, String sort, Sort.Direction direction) {
        Page<Observation> page = observationRepository.findAll(new PageRequest(pageNumber, RecordsPageDto.RECORDS_PAGE_SIZE, new Sort(direction, sort)));
        List<FlatObservationDto> flatObservationDtos = page.getContent().stream()
                .map(this::convertToFlatObservationDto)
                .collect(Collectors.toList());
        return new RecordsPageDto<>(flatObservationDtos, page.getTotalElements(), page.getTotalPages());
    }

    private FlatObservationDto convertToFlatObservationDto(Observation o) {
        return new FlatObservationDto.Builder()
                .id(o.getId())
                .dateTimestamp(o.getDateTimestamp())
                .restricted(o.isRestricted())
                .author(o.getAuthor())
                .votesCount(o.getVotesCount())
                .speciesClass(o.getSpeciesStub().getSpeciesClass().name())
                .speciesLatinName(o.getSpeciesStub().getLatinName())
                .longitude(o.getLocation().getX())
                .latitude(o.getLocation().getY())
                .build();
    }

    private ExistingObservationDto convertToExistingObservationDto(Observation observation) {
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

    private AggregatedObservationDto convertToAggregatedObservationDto(AggregatedObservation aggregatedObservation) {
        WGS84Point point = geoHashService.getPointFromGeoHash(aggregatedObservation.get_id());
        return new AggregatedObservationDto(
                aggregatedObservation.getCount(),
                point.getLongitude(),
                point.getLatitude()
        );
    }
}
