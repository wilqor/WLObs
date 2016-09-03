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
public final class NewObservationDto {
    @SerializedName("dateUtcTimestamp")
    @Expose
    private long dateUtcTimestamp;

    @SerializedName("speciesStub")
    @Expose
    private SpeciesStub speciesStub;

    @SerializedName("restricted")
    @Expose
    private boolean restricted;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    public long getDateUtcTimestamp() {
        return dateUtcTimestamp;
    }

    public void setDateUtcTimestamp(long dateUtcTimestamp) {
        this.dateUtcTimestamp = dateUtcTimestamp;
    }

    public SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    public void setSpeciesStub(SpeciesStub speciesStub) {
        this.speciesStub = speciesStub;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
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

    @Override
    public String toString() {
        return "NewObservationDto{" +
                "dateUtcTimestamp=" + dateUtcTimestamp +
                ", speciesStub=" + speciesStub +
                ", restricted=" + restricted +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
