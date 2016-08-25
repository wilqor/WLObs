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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author wilqor
 */
@Document
public class Observation {
    @Id
    private String id;

    @Indexed
    private long dateTimestamp;

    private boolean restricted;

    @Indexed
    private String author;

    private long votesCount;

    private String geoHash;

    /**
     * location is stored in GeoJSON format.
     * {
     * "type" : "Point",
     * "coordinates" : [ x, y ]
     * }
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    @Indexed
    private SpeciesStub speciesStub;

    public static class SpeciesStub {
        @NotNull
        @JsonProperty("class")
        private Species.Class speciesClass;
        @NotEmpty
        @JsonProperty("latin_name")
        private String latinName;

        public SpeciesStub() {
        }

        public SpeciesStub(Species.Class speciesClass, String latinName) {
            this.speciesClass = speciesClass;
            this.latinName = latinName;
        }

        public Species.Class getSpeciesClass() {
            return speciesClass;
        }

        public String getLatinName() {
            return latinName;
        }

        public void setSpeciesClass(Species.Class speciesClass) {
            this.speciesClass = speciesClass;
        }

        public void setLatinName(String latinName) {
            this.latinName = latinName;
        }

        @Override
        public String toString() {
            return "SpeciesStub{" +
                    "speciesClass=" + speciesClass +
                    ", latinName='" + latinName + '\'' +
                    '}';
        }
    }

    public String getId() {
        return id;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(long votesCount) {
        this.votesCount = votesCount;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public SpeciesStub getSpeciesStub() {
        return speciesStub;
    }

    public void setSpeciesStub(SpeciesStub speciesStub) {
        this.speciesStub = speciesStub;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "id='" + id + '\'' +
                ", date=" + dateTimestamp +
                ", restricted=" + restricted +
                ", author='" + author + '\'' +
                ", votesCount=" + votesCount +
                ", geoHash='" + geoHash + '\'' +
                ", location=" + location +
                '}';
    }

    public static class Builder {
        private long dateTimestamp;
        private boolean restricted;
        private String author;
        private long votesCount;
        private String geoHash;
        private GeoJsonPoint location;
        private SpeciesStub speciesStub;

        public Builder() {
            dateTimestamp = 0;
            restricted = false;
            author = "";
            votesCount = 0;
            geoHash = "";
            location = new GeoJsonPoint(0.0d, 0.0d);
            speciesStub = new SpeciesStub();
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

        public Builder geoHash(String geoHash) {
            this.geoHash = geoHash;
            return this;
        }

        public Builder location(GeoJsonPoint location) {
            this.location = location;
            return this;
        }

        public Builder speciesStub(SpeciesStub speciesStub) {
            this.speciesStub = speciesStub;
            return this;
        }

        public Observation build() {
            Observation created = new Observation();
            created.setDateTimestamp(dateTimestamp);
            created.setRestricted(restricted);
            created.setAuthor(author);
            created.setVotesCount(votesCount);
            created.setGeoHash(geoHash);
            created.setLocation(location);
            created.setSpeciesStub(speciesStub);
            return created;
        }
    }
}
