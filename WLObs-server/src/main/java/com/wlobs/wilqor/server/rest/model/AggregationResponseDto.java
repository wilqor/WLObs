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

package com.wlobs.wilqor.server.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author wilqor
 */
public class AggregationResponseDto {
    @JsonProperty
    private final List<ExistingObservationDto> rawObservations;

    @JsonProperty
    private final List<AggregatedObservationDto> aggregatedObservations;

    public AggregationResponseDto(List<ExistingObservationDto> rawObservations, List<AggregatedObservationDto> aggregatedObservations) {
        this.rawObservations = rawObservations;
        this.aggregatedObservations = aggregatedObservations;
    }

    public List<ExistingObservationDto> getRawObservations() {
        return rawObservations;
    }

    public List<AggregatedObservationDto> getAggregatedObservations() {
        return aggregatedObservations;
    }

    @Override
    public String toString() {
        return "AggregationResponseDto{" +
                "rawObservations=" + rawObservations +
                ", aggregatedObservations=" + aggregatedObservations +
                '}';
    }
}
