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

import com.fernandocejas.arrow.optional.Optional;

import java.util.Arrays;
import java.util.List;

/**
 * @author wilqor
 */
final class CompositeStringInputValidator implements InputValidator<String> {
    private final List<InputValidator<String>> validators;

    @SafeVarargs
    CompositeStringInputValidator(InputValidator<String>... validators) {
        this.validators = Arrays.asList(validators);
    }

    @Override
    public Optional<ValidationError> validate(String input) {
        Optional<ValidationError> result = Optional.absent();
        for (InputValidator<String> v : validators) {
            result = v.validate(input);
            if (result.isPresent()) {
                break;
            }
        }
        return result;
    }
}
