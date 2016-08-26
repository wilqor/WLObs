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
public class FlatSpeciesDto {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("species_class")
    private final String speciesClass;

    @JsonProperty("latin_name")
    private final String latinName;

    @JsonProperty("english_name")
    private final String englishName;

    public FlatSpeciesDto(String id, String speciesClass, String latinName, String englishName) {
        this.id = id;
        this.speciesClass = speciesClass;
        this.latinName = latinName;
        this.englishName = englishName;
    }

    public String getId() {
        return id;
    }

    public String getLatinName() {
        return latinName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getSpeciesClass() {
        return speciesClass;
    }

    @Override
    public String toString() {
        return "FlatSpeciesDto{" +
                "id='" + id + '\'' +
                ", speciesClass='" + speciesClass + '\'' +
                ", latinName='" + latinName + '\'' +
                ", englishName='" + englishName + '\'' +
                '}';
    }
}
