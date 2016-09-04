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

import java.util.Map;

/**
 * @author wilqor
 */
public class SpeciesCountDto {
    @SerializedName("speciesCount")
    @Expose
    private Map<SpeciesStub.Class, Long> speciesCount;

    public Map<SpeciesStub.Class, Long> getSpeciesCount() {
        return speciesCount;
    }

    public void setSpeciesCount(Map<SpeciesStub.Class, Long> speciesCount) {
        this.speciesCount = speciesCount;
    }

    @Override
    public String toString() {
        return "SpeciesCountDto{" +
                "speciesCount=" + speciesCount +
                '}';
    }
}
