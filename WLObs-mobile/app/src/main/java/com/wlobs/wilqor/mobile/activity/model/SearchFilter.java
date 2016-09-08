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

package com.wlobs.wilqor.mobile.activity.model;

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.rest.model.AggregationRequestDto;

import java.io.Serializable;

/**
 * @author wilqor
 */
public class SearchFilter implements Serializable {
    private final Optional<AggregationRequestDto.TimeRange> timeRangeOptional;
    private final Optional<SpeciesSelection> speciesSelectionOptional;

    public SearchFilter(Optional<AggregationRequestDto.TimeRange> timeRangeOptional, Optional<SpeciesSelection> speciesSelectionOptional) {
        this.timeRangeOptional = timeRangeOptional;
        this.speciesSelectionOptional = speciesSelectionOptional;
    }

    public Optional<AggregationRequestDto.TimeRange> getTimeRangeOptional() {
        return timeRangeOptional;
    }

    public Optional<SpeciesSelection> getSpeciesSelectionOptional() {
        return speciesSelectionOptional;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "timeRangeOptional=" + timeRangeOptional +
                ", speciesSelectionOptional=" + speciesSelectionOptional +
                '}';
    }
}
