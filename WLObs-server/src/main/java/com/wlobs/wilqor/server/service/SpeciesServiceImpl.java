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

import com.wlobs.wilqor.server.config.LocaleConstants;
import com.wlobs.wilqor.server.persistence.model.Observation;
import com.wlobs.wilqor.server.persistence.model.Species;
import com.wlobs.wilqor.server.persistence.repository.SpeciesRepository;
import com.wlobs.wilqor.server.rest.model.LocalizedSpeciesDto;
import com.wlobs.wilqor.server.rest.model.SpeciesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wilqor
 */
@Service
public class SpeciesServiceImpl implements SpeciesService {
    private final SpeciesRepository speciesRepository;

    @Autowired
    public SpeciesServiceImpl(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Override
    public void addSpecies(List<SpeciesDto> speciesDtoList) {
        speciesDtoList.forEach(s -> speciesRepository.upsertSpecies(s.getSpeciesClass(), s.getLatinName(), getLocaleNamesMap(s)));
    }

    @Override
    public List<LocalizedSpeciesDto> findSpeciesForLocaleAndClass(Locale locale, Species.Class speciesClass) {
        return speciesRepository.findBySpeciesClass(speciesClass)
                .stream()
                .map(f -> new LocalizedSpeciesDto(
                        f.getSpeciesClass(),
                        f.getLatinName(),
                        getRequestedOrDefaultLocale(locale, f)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Species> findSpeciesForStub(Observation.SpeciesStub speciesStub) {
        return speciesRepository.findBySpeciesClassAndLatinName(speciesStub.getSpeciesClass(), speciesStub.getLatinName());
    }

    private String getRequestedOrDefaultLocale(Locale locale, Species species) {
        return species.getLocalizedNames().getOrDefault(locale, species.getLocalizedNames().get(LocaleConstants.DEFAULT_LOCALE));
    }

    private Map<Locale, String> getLocaleNamesMap(SpeciesDto speciesDto) {
        return speciesDto.getLocalizedNames().entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                e -> new Locale(e.getKey()),
                                Map.Entry::getValue
                        )
                );
    }
}
