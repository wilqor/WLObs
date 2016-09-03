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
public final class SpeciesStub {
    @SerializedName("latinName")
    @Expose
    private String latinName;

    @SerializedName("speciesClass")
    @Expose
    private Class speciesClass;

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public enum Class {
        FISH,
        AMPHIBIAN,
        REPTILE,
        BIRD,
        MAMMAL
    }

    @Override
    public String toString() {
        return "SpeciesStub{" +
                "latinName='" + latinName + '\'' +
                ", speciesClass=" + speciesClass +
                '}';
    }
}
