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

package com.wlobs.wilqor.server.rest;

import com.wlobs.wilqor.server.config.LocaleConstants;
import com.wlobs.wilqor.server.persistence.model.Species;
import com.wlobs.wilqor.server.rest.model.LocalizedSpeciesDto;
import com.wlobs.wilqor.server.rest.model.SpeciesListDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/species")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class SpeciesController {
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public String addSpecies(@RequestBody @Valid SpeciesListDto speciesListDto) {
        // TODO actually add received species
        return "add species";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{class}")
    public List<LocalizedSpeciesDto> getSpecies(@RequestHeader(value = "Accept-Language", defaultValue = "en") String locale, @PathVariable("class") Species.Class speciesClass) {
        Locale target = getTargetLocale(locale);
        // TODO return actual species retrieved for provided class and locale
        return Arrays.asList(
                new LocalizedSpeciesDto(Species.Class.MAMMAL, "canis lupus", "wolf")
        );
    }

    private Locale getTargetLocale(String requestHeaderLocale) {
        Optional<Locale> matched = Optional.ofNullable(Locale.lookup(Locale.LanguageRange.parse(requestHeaderLocale), LocaleConstants.supportedLocale));
        return matched.orElse(LocaleConstants.DEFAULT_LOCALE);
    }
}
