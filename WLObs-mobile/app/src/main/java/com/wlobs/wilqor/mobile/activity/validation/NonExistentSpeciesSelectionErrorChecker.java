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

package com.wlobs.wilqor.mobile.activity.validation;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.activity.validation.model.SpeciesSelection;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;

/**
 * @author wilqor
 */
public final class NonExistentSpeciesSelectionErrorChecker implements ErrorChecker<SpeciesSelection> {
    @Override
    public boolean hasError(SpeciesSelection input) {
        Species matchedSpecies = SQLite.select()
                .from(Species.class)
                .where(Species_Table.speciesClass.eq(input.getSpeciesClass()))
                .and(Species_Table.name.eq(input.getSpeciesName()))
                .querySingle();
        return matchedSpecies == null;
    }
}
