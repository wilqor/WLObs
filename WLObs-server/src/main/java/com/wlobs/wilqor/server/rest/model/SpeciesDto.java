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
import com.wlobs.wilqor.server.rest.validation.ContainsEnglishLocale;
import com.wlobs.wilqor.server.rest.validation.HasOnlySupportedLocale;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * @author wilqor
 */
public class SpeciesDto {
    @NotNull
    @JsonProperty
    private Species.Class speciesClass;

    @NotNull
    @Pattern(regexp = "^[A-Za-z ]*$")
    @JsonProperty
    private String latinName;

    @NotEmpty
    @ContainsEnglishLocale
    @HasOnlySupportedLocale
    @JsonProperty
    private Map<String, String> localizedNames;

    public Species.Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(Species.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public Map<String, String> getLocalizedNames() {
        return localizedNames;
    }

    public void setLocalizedNames(Map<String, String> localizedNames) {
        this.localizedNames = localizedNames;
    }

    @Override
    public String toString() {
        return "SpeciesDto{" +
                "speciesClass=" + speciesClass +
                ", latinName='" + latinName + '\'' +
                ", localizedNames=" + localizedNames +
                '}';
    }
}
