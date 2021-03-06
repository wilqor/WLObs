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
import com.wlobs.wilqor.server.persistence.model.Species;
import com.wlobs.wilqor.server.service.model.AggregationRequest;

/**
 * @author wilqor
 */
public class AggregationRequestDto {
    @JsonProperty
    private AggregationRequest.Area area;

    @JsonProperty
    private AggregationRequest.TimeRange timeRange;

    @JsonProperty
    private Species.Class speciesClass;

    @JsonProperty
    private String speciesLatinName;

    public AggregationRequest.Area getArea() {
        return area;
    }

    public void setArea(AggregationRequest.Area area) {
        this.area = area;
    }

    public AggregationRequest.TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(AggregationRequest.TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public Species.Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(Species.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public String getSpeciesLatinName() {
        return speciesLatinName;
    }

    public void setSpeciesLatinName(String speciesLatinName) {
        this.speciesLatinName = speciesLatinName;
    }

    @Override
    public String toString() {
        return "AggregationRequestDto{" +
                "area=" + area +
                ", timeRange=" + timeRange +
                ", speciesClass=" + speciesClass +
                ", speciesLatinName='" + speciesLatinName + '\'' +
                '}';
    }
}
