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

package com.wlobs.wilqor.mobile.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author wilqor
 */
public final class ExistingVoteDto {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("observationId")
    @Expose
    private String observationId;

    @SerializedName("dateUtcTimestamp")
    @Expose
    private long dateUtcTimestamp;

    @SerializedName("voter")
    @Expose
    private String voter;

    @SerializedName("observationOwner")
    @Expose
    private String observationOwner;

    @SerializedName("speciesStub")
    @Expose
    private SpeciesStub speciesStub;

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

    public SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    public void setSpeciesStub(SpeciesStub speciesStub) {
        this.speciesStub = speciesStub;
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
}
