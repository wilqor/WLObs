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

package com.wlobs.wilqor.server.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wilqor
 */
@Document
public class Vote {
    @Id
    private String id;

    @Indexed
    private String observationId;

    private long dateUtcTimestamp;

    @Indexed
    private String voter;

    private String observationOwner;

    private Observation.SpeciesStub speciesStub;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public void setDateUtcTimestamp(long dateUtcTimestamp) {
        this.dateUtcTimestamp = dateUtcTimestamp;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }

    public String getObservationOwner() {
        return observationOwner;
    }

    public void setObservationOwner(String observationOwner) {
        this.observationOwner = observationOwner;
    }

    public Observation.SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    public void setSpeciesStub(Observation.SpeciesStub speciesStub) {
        this.speciesStub = speciesStub;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id='" + id + '\'' +
                ", observationId='" + observationId + '\'' +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", voter='" + voter + '\'' +
                ", observationOwner='" + observationOwner + '\'' +
                ", speciesStub=" + speciesStub +
                '}';
    }

    public static class Builder {
        private String observationId;
        private long dateUtcTimestamp;
        private String voter;
        private String observationOwner;
        private Observation.SpeciesStub speciesStub;

        public Builder() {
            observationId = "";
            dateUtcTimestamp = 0;
            voter = "";
            observationOwner = "";
            speciesStub = new Observation.SpeciesStub();
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

        public Vote build() {
            Vote result = new Vote();
            result.setObservationId(observationId);
            result.setDateUtcTimestamp(dateUtcTimestamp);
            result.setVoter(voter);
            result.setObservationOwner(observationOwner);
            result.setSpeciesStub(speciesStub);
            return result;
        }
    }
}
