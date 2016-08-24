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
import com.wlobs.wilqor.server.persistence.model.Observation;

/**
 * @author wilqor
 */
public class ExistingObservationDto {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("author")
    private final String author;

    @JsonProperty("votes_count")
    private final long votesCount;

    @JsonProperty("date_utc_timestamp")
    private final long dateUtcTimestamp;

    @JsonProperty("species_stub")
    private final Observation.SpeciesStub speciesStub;

    @JsonProperty("restricted")
    private final boolean restricted;

    @JsonProperty("longitude")
    private final double longitude;

    @JsonProperty("latitude")
    private final double latitude;

    ExistingObservationDto(String id,
                           String author,
                           long votesCount,
                           long dateUtcTimestamp,
                           Observation.SpeciesStub speciesStub,
                           boolean restricted,
                           double longitude,
                           double latitude) {
        this.id = id;
        this.author = author;
        this.votesCount = votesCount;
        this.dateUtcTimestamp = dateUtcTimestamp;
        this.speciesStub = speciesStub;
        this.restricted = restricted;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public long getVotesCount() {
        return votesCount;
    }

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public Observation.SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "ExistingObservationDto{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", votesCount=" + votesCount +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", speciesStub=" + speciesStub +
                ", restricted=" + restricted +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public static class Builder {
        private String id;
        private String author;
        private long votesCount;
        private long dateUtcTimestamp;
        private Observation.SpeciesStub speciesStub;
        private boolean restricted;
        private double longitude;
        private double latitude;

        public Builder() {
            id = "";
            author = "";
            votesCount = 0;
            dateUtcTimestamp = 0;
            speciesStub = new Observation.SpeciesStub();
            restricted = false;
            longitude = 0.0d;
            latitude = 0.0d;
        }

        public Builder id(String id) {
            this.id = id;
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

        public Builder dateUtcTimestamp(long dateUtcTimestamp) {
            this.dateUtcTimestamp = dateUtcTimestamp;
            return this;
        }

        public Builder speciesStub(Observation.SpeciesStub speciesStub) {
            this.speciesStub = speciesStub;
            return this;
        }

        public Builder restricted(boolean restricted) {
            this.restricted = restricted;
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

        public ExistingObservationDto build() {
            return new ExistingObservationDto(
                    id,
                    author,
                    votesCount,
                    dateUtcTimestamp,
                    speciesStub,
                    restricted,
                    longitude,
                    latitude
            );
        }
    }
}
