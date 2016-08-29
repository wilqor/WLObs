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
public class FlatVoteDto {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String observationId;

    @JsonProperty
    private final long dateUtcTimestamp;

    @JsonProperty
    private final String voter;

    @JsonProperty
    private final String observationOwner;

    @JsonProperty
    private final String speciesClass;

    @JsonProperty
    private final String speciesLatinName;

    private FlatVoteDto(String id, String observationId, long dateUtcTimestamp, String voter, String observationOwner, String speciesClass, String speciesLatinName) {
        this.id = id;
        this.observationId = observationId;
        this.dateUtcTimestamp = dateUtcTimestamp;
        this.voter = voter;
        this.observationOwner = observationOwner;
        this.speciesClass = speciesClass;
        this.speciesLatinName = speciesLatinName;
    }

    public String getId() {
        return id;
    }

    public String getObservationId() {
        return observationId;
    }

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public String getVoter() {
        return voter;
    }

    public String getObservationOwner() {
        return observationOwner;
    }

    public String getSpeciesClass() {
        return speciesClass;
    }

    public String getSpeciesLatinName() {
        return speciesLatinName;
    }

    @Override
    public String toString() {
        return "FlatVoteDto{" +
                "id='" + id + '\'' +
                ", observationId='" + observationId + '\'' +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", voter='" + voter + '\'' +
                ", observationOwner='" + observationOwner + '\'' +
                ", speciesClass='" + speciesClass + '\'' +
                ", speciesLatinName='" + speciesLatinName + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String observationId;
        private long dateUtcTimestamp;
        private String voter;
        private String observationOwner;
        private String speciesClass;
        private String speciesLatinName;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder observationId(String observationId) {
            this.observationId = observationId;
            return this;
        }

        public Builder dateUtcTimestamp(long dateUtcTimestamp) {
            this.dateUtcTimestamp = dateUtcTimestamp;
            return this;
        }

        public Builder voter(String voter) {
            this.voter = voter;
            return this;
        }

        public Builder observationOwner(String observationOwner) {
            this.observationOwner = observationOwner;
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

        public FlatVoteDto build() {
            return new FlatVoteDto(
                    id,
                    observationId,
                    dateUtcTimestamp,
                    voter,
                    observationOwner,
                    speciesClass,
                    speciesLatinName
            );
        }
    }
}
