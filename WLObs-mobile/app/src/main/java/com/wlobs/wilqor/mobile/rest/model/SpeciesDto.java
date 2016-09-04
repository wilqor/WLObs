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
public final class SpeciesDto {
    @SerializedName("latinName")
    @Expose
    private String latinName;

    @SerializedName("localizedName")
    @Expose
    private String name;

    @SerializedName("speciesClass")
    @Expose
    private SpeciesStub.Class speciesClass;

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpeciesStub.Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(SpeciesStub.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    @Override
    public String toString() {
        return "SpeciesDto{" +
                "latinName='" + latinName + '\'' +
                ", name='" + name + '\'' +
                ", speciesClass=" + speciesClass +
                '}';
    }
}
