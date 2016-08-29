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

/**
 * @author wilqor
 */
public class FlatObservationDto {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final long dateTimestamp;

    @JsonProperty
    private final boolean restricted;

    @JsonProperty
    private final String author;

    @JsonProperty
    private final long votesCount;

    @JsonProperty
    private final String speciesClass;

    @JsonProperty
    private final String speciesLatinName;

    @JsonProperty
    private final double longitude;

    @JsonProperty
    private final double latitude;

    private FlatObservationDto(String id,
                               long dateTimestamp,
                               boolean restricted,
                               String author,
                               long votesCount,
                               String speciesClass,
                               String speciesLatinName,
                               double longitude,
                               double latitude) {
        this.id = id;
        this.dateTimestamp = dateTimestamp;
        this.restricted = restricted;
        this.author = author;
        this.votesCount = votesCount;
        this.speciesClass = speciesClass;
        this.speciesLatinName = speciesLatinName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public String getAuthor() {
        return author;
    }

    public long getVotesCount() {
        return votesCount;
    }

    public String getSpeciesClass() {
        return speciesClass;
    }

    public String getSpeciesLatinName() {
        return speciesLatinName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "FlatObservationDto{" +
                "id='" + id + '\'' +
                ", dateTimestamp=" + dateTimestamp +
                ", restricted=" + restricted +
                ", author='" + author + '\'' +
                ", votesCount=" + votesCount +
                ", speciesClass='" + speciesClass + '\'' +
                ", speciesLatinName='" + speciesLatinName + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public static class Builder {
        private String id;
        private long dateTimestamp;
        private boolean restricted;
        private String author;
        private long votesCount;
        private String speciesClass;
        private String speciesLatinName;
        private double longitude;
        private double latitude;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder dateTimestamp(long dateTimestamp) {
            this.dateTimestamp = dateTimestamp;
            return this;
        }

        public Builder restricted(boolean restricted) {
            this.restricted = restricted;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder votesCount(long votesCount) {
            this.votesCount = votesCount;
            return this;
        }

        public Builder speciesClass(String speciesClass) {
            this.speciesClass = speciesClass;
            return this;
        }

        public Builder speciesLatinName(String speciesLatinName) {
            this.speciesLatinName = speciesLatinName;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public FlatObservationDto build() {
            return new FlatObservationDto(
                    id,
                    dateTimestamp,
                    restricted,
                    author,
                    votesCount,
                    speciesClass,
                    speciesLatinName,
                    longitude,
                    latitude
            );
        }
    }
}
