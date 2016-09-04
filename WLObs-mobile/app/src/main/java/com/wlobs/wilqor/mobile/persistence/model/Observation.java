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
import com.wlobs.wilqor.mobile.rest.model.NewObservationDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

/**
 * @author wilqor
 */
@Table(database = LocalDatabase.class)
public class Observation extends BaseModel {
    public static final String ID_NOT_SET = "NOT_SET";

    @Column
    @PrimaryKey(autoincrement = true)
    private int localId;

    @Column(defaultValue = "\"NOT_SET\"")
    private String remoteId;

    @Column
    private String author;

    @Column
    private long dateUtcTimestamp;

    @Column(defaultValue = "0L")
    private Long votesCount;

    @Column(defaultValue = "0")
    private Boolean restricted;

    @Column
    private double longitude;

    @Column
    private double latitude;

    @Column(defaultValue = "0")
    private Boolean deleted;

    @Column(defaultValue = "0")
    private Boolean restrictionChanged;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    private Species species;

    public Observation() {
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public void setDateUtcTimestamp(long dateUtcTimestamp) {
        this.dateUtcTimestamp = dateUtcTimestamp;
    }

    public Long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Long votesCount) {
        this.votesCount = votesCount;
    }

    public Boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isRestrictionChanged() {
        return restrictionChanged;
    }

    public void setRestrictionChanged(Boolean restrictionChanged) {
        this.restrictionChanged = restrictionChanged;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public boolean isRemoteItNotSet() {
        return "NOT_SET".equals(remoteId);
    }

    @Override
    public String toString() {
        return "Observation{" +
                "localId=" + localId +
                ", remoteId='" + remoteId + '\'' +
                ", author='" + author + '\'' +
                ", dateUtcTimestamp=" + dateUtcTimestamp +
                ", votesCount=" + votesCount +
                ", restricted=" + restricted +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", deleted=" + deleted +
                ", restrictionChanged=" + restrictionChanged +
                ", species=" + species +
                '}';
    }

    public NewObservationDto asNewObservationDto() {
        NewObservationDto dto = new NewObservationDto();
        dto.setDateUtcTimestamp(dateUtcTimestamp);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        dto.setRestricted(restricted);
        SpeciesStub speciesStub = new SpeciesStub();
        speciesStub.setLatinName(species.getLatinName());
        speciesStub.setSpeciesClass(species.getSpeciesClass());
        dto.setSpeciesStub(speciesStub);
        return dto;
    }
}
