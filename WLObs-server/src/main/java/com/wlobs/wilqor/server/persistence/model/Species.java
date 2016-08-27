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

import java.util.Locale;
import java.util.Map;

/**
 * @author wilqor
 */
@Document
public class Species {
    @Id
    private String id;

    @Indexed
    private Class speciesClass;

    @Indexed
    private String latinName;

    @Indexed
    private int observationsCount;

    @Indexed
    private Map<Locale, String> localizedNames;

    /**
     * Localized on the client side, since all values are known in advance
     */
    public enum Class {
        FISH,
        AMPHIBIAN,
        REPTILE,
        BIRD,
        MAMMAL
    }

    public void setSpeciesClass(Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public void setLocalizedNames(Map<Locale, String> localizedNames) {
        this.localizedNames = localizedNames;
    }

    public String getId() {
        return id;
    }

    public Class getSpeciesClass() {
        return speciesClass;
    }

    public String getLatinName() {
        return latinName;
    }

    public Map<Locale, String> getLocalizedNames() {
        return localizedNames;
    }

    public int getObservationsCount() {
        return observationsCount;
    }

    public void setObservationsCount(int observationsCount) {
        this.observationsCount = observationsCount;
    }

    @Override
    public String toString() {
        return "Species{" +
                "id='" + id + '\'' +
                ", speciesClass=" + speciesClass +
                ", latinName='" + latinName + '\'' +
                ", observationsCount=" + observationsCount +
                ", localizedNames=" + localizedNames +
                '}';
    }
}
