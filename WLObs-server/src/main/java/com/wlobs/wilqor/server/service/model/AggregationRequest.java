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

package com.wlobs.wilqor.server.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wlobs.wilqor.server.persistence.model.Species;

import java.util.Optional;

/**
 * @author wilqor
 */
public class AggregationRequest {
    private final Area area;
    private final Optional<TimeRange> timeRangeOptional;
    private final Optional<Species.Class> speciesClassOptional;
    private final Optional<String> speciesLatinNameOptional;

    private AggregationRequest(Area area, Optional<TimeRange> timeRangeOptional, Optional<Species.Class> speciesClassOptional, Optional<String> speciesLatinNameOptional) {
        this.area = area;
        this.timeRangeOptional = timeRangeOptional;
        this.speciesClassOptional = speciesClassOptional;
        this.speciesLatinNameOptional = speciesLatinNameOptional;
    }

    public Area getArea() {
        return area;
    }

    public Optional<TimeRange> getTimeRangeOptional() {
        return timeRangeOptional;
    }

    public Optional<Species.Class> getSpeciesClassOptional() {
        return speciesClassOptional;
    }

    public Optional<String> getSpeciesLatinNameOptional() {
        return speciesLatinNameOptional;
    }

    @Override
    public String toString() {
        return "AggregationRequest{" +
                "area=" + area +
                ", timeRangeOptional=" + timeRangeOptional +
                ", speciesClassOptional=" + speciesClassOptional +
                ", speciesLatinNameOptional=" + speciesLatinNameOptional +
                '}';
    }

    public static class Area {
        @JsonProperty("bottom")
        private double bottom;

        @JsonProperty("top")
        private double top;

        @JsonProperty("left")
        private double left;

        @JsonProperty("right")
        private double right;

        public double getBottom() {
            return bottom;
        }

        public void setBottom(double bottom) {
            this.bottom = bottom;
        }

        public double getTop() {
            return top;
        }

        public void setTop(double top) {
            this.top = top;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getRight() {
            return right;
        }

        public void setRight(double right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return "Area{" +
                    "bottom=" + bottom +
                    ", top=" + top +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    public static class TimeRange {
        @JsonProperty("date_from_utc_timestamp")
        private double dateFromUtcTimestamp;

        @JsonProperty("date_to_utc_timestamp")
        private double dateToUtcTimestamp;

        public double getDateFromUtcTimestamp() {
            return dateFromUtcTimestamp;
        }

        public void setDateFromUtcTimestamp(double dateFromUtcTimestamp) {
            this.dateFromUtcTimestamp = dateFromUtcTimestamp;
        }

        public double getDateToUtcTimestamp() {
            return dateToUtcTimestamp;
        }

        public void setDateToUtcTimestamp(double dateToUtcTimestamp) {
            this.dateToUtcTimestamp = dateToUtcTimestamp;
        }

        @Override
        public String toString() {
            return "TimeRange{" +
                    "dateFromUtcTimestamp=" + dateFromUtcTimestamp +
                    ", dateToUtcTimestamp=" + dateToUtcTimestamp +
                    '}';
        }
    }

    public static class Builder {
        private Area area;
        private TimeRange timeRange;
        private Species.Class speciesClass;
        private String speciesLatinName;

        public Builder() {
            this.area = new Area();
        }

        public Builder area(Area area) {
            this.area = area;
            return this;
        }

        public Builder timeRange(TimeRange timeRange) {
            this.timeRange = timeRange;
            return this;
        }

        public Builder speciesClass(Species.Class speciesClass) {
            this.speciesClass = speciesClass;
            return this;
        }

        public Builder speciesLatinName(String speciesLatinName) {
            this.speciesLatinName = speciesLatinName;
            return this;
        }

        public AggregationRequest build() {
            return new AggregationRequest(
                    area,
                    Optional.ofNullable(timeRange),
                    Optional.ofNullable(speciesClass),
                    Optional.ofNullable(speciesLatinName)
            );
        }
    }
}
