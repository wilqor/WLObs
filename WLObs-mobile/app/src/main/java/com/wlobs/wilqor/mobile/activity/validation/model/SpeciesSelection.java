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

package com.wlobs.wilqor.mobile.activity.validation.model;

import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

/**
 * @author wilqor
 */
public final class SpeciesSelection {
    private final SpeciesStub.Class speciesClass;
    private final String speciesName;

    public SpeciesSelection(SpeciesStub.Class speciesClass, String speciesName) {
        this.speciesClass = speciesClass;
        this.speciesName = speciesName;
    }

    public SpeciesStub.Class getSpeciesClass() {
        return speciesClass;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    @Override
    public String toString() {
        return "SpeciesSelection{" +
                "speciesClass=" + speciesClass +
                ", speciesName='" + speciesName + '\'' +
                '}';
    }
}
