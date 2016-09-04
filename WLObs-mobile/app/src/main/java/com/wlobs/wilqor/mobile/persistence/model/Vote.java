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

package com.wlobs.wilqor.mobile.persistence.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.wlobs.wilqor.mobile.persistence.db.LocalDatabase;
import com.wlobs.wilqor.mobile.rest.model.NewVoteDto;

/**
 * @author wilqor
 */
@Table(database = LocalDatabase.class)
public class Vote extends BaseModel {
    public static final String ID_NOT_SET = "NOT_SET";

    @Column
    @PrimaryKey(autoincrement = true)
    private int localId;

    @Column(defaultValue = "\"NOT_SET\"")
    private String remoteId;

    @Column
    private String voter;

    @Column
    private long dateUtcTimestamp;

    @Column
    private String observationOwner;

    @Column
    private String remoteObservationId;

    @Column(defaultValue = "0")
    private Boolean deleted;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    private Species species;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public void setDateUtcTimestamp(long dateUtcTimestamp) {
        this.dateUtcTimestamp = dateUtcTimestamp;
    }

    public String getObservationOwner() {
        return observationOwner;
    }

    public void setObservationOwner(String observationOwner) {
        this.observationOwner = observationOwner;
    }

    public String getRemoteObservationId() {
        return remoteObservationId;
    }

    public void setRemoteObservationId(String remoteObservationId) {
        this.remoteObservationId = remoteObservationId;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public boolean isRemoteIdNotSet() {
        return "NOT_SET".equals(remoteId);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "localId=" + localId +
                ", remoteId='" + remoteId + '\'' +
                ", voter='" + voter + '\'' +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", observationOwner='" + observationOwner + '\'' +
                ", remoteObservationId='" + remoteObservationId + '\'' +
                ", deleted=" + deleted +
                ", species=" + species +
                '}';
    }

    public NewVoteDto asNewVoteDto() {
        return new NewVoteDto(remoteObservationId, dateUtcTimestamp);
    }
}
