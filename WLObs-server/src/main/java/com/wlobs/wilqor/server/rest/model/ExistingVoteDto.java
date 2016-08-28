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

import com.wlobs.wilqor.server.persistence.model.Observation;

/**
 * @author wilqor
 */
public class ExistingVoteDto {
    private final String id;
    private final String observationId;
    private final long dateUtcTimestamp;
    private final String voter;
    private final String observationOwner;
    private final Observation.SpeciesStub speciesStub;

    ExistingVoteDto(String id, String observationId, long dateUtcTimestamp, String voter, String observationOwner, Observation.SpeciesStub speciesStub) {
        this.id = id;
        this.observationId = observationId;
        this.dateUtcTimestamp = dateUtcTimestamp;
        this.voter = voter;
        this.observationOwner = observationOwner;
        this.speciesStub = speciesStub;
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

    public Observation.SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    @Override
    public String toString() {
        return "ExistingVoteDto{" +
                "id='" + id + '\'' +
                ", observationId='" + observationId + '\'' +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", voter='" + voter + '\'' +
                ", observationOwner='" + observationOwner + '\'' +
                ", speciesStub=" + speciesStub +
                '}';
    }

    public static class Builder {
        private String id;
        private String observationId;
        private long dateUtcTimestamp;
        private String voter;
        private String observationOwner;
        private Observation.SpeciesStub speciesStub;

        public Builder() {
            id = "";
            observationId = "";
            dateUtcTimestamp = 0;
            voter = "";
            observationOwner = "";
            speciesStub = new Observation.SpeciesStub();
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

        public Builder speciesStub(Observation.SpeciesStub speciesStub) {
            this.speciesStub = speciesStub;
            return this;
        }

        public ExistingVoteDto build() {
            return new ExistingVoteDto(
                    id,
                    observationId,
                    dateUtcTimestamp,
                    voter,
                    observationOwner,
                    speciesStub
            );
        }
    }
}
