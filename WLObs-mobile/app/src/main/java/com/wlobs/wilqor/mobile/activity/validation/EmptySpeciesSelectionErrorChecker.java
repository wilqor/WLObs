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

import com.fernandocejas.arrow.strings.Strings;
import com.wlobs.wilqor.mobile.activity.model.SpeciesSelection;

/**
 * @author wilqor
 */
public class EmptySpeciesSelectionErrorChecker implements ErrorChecker<SpeciesSelection> {
    @Override
    public boolean hasError(SpeciesSelection input) {
        return Strings.isNullOrEmpty(input.getSpeciesName());
    }
}
