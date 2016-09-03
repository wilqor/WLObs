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

package com.wlobs.wilqor.mobile.persistence.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.wlobs.wilqor.mobile.persistence.db.LocalDatabase;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

/**
 * @author wilqor
 */
@Table(database = LocalDatabase.class)
public class Species extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private int localId;

    @Column
    private SpeciesStub.Class speciesClass;

    @Column
    private String latinName;

    @Column
    private String name;

    public SpeciesStub.Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(SpeciesStub.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

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

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    @Override
    public String toString() {
        return "Species{" +
                "speciesClass=" + speciesClass +
                ", latinName='" + latinName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
