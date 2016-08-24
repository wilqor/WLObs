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
import com.wlobs.wilqor.server.persistence.model.Species;

/**
 * @author wilqor
 */
public class LocalizedSpeciesDto {
    @JsonProperty("class")
    private final Species.Class speciesClass;

    @JsonProperty("latin_name")
    private final String latinName;

    @JsonProperty("localized_name")
    private final String localizedName;

    public LocalizedSpeciesDto(Species.Class speciesClass, String latinName, String localizedName) {
        this.speciesClass = speciesClass;
        this.latinName = latinName;
        this.localizedName = localizedName;
    }

    @Override
    public String toString() {
        return "LocalizedSpeciesDto{" +
                "speciesClass=" + speciesClass +
                ", latinName='" + latinName + '\'' +
                ", localizedName='" + localizedName + '\'' +
                '}';
    }
}
