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

package com.wlobs.wilqor.server.rest.validation;

import com.wlobs.wilqor.server.config.LocaleConstants;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * @author wilqor
 */
class EnglishLocaleValidator implements ConstraintValidator<ContainsEnglishLocale, Map<String, String>> {
    @Override
    public void initialize(ContainsEnglishLocale constraintAnnotation) {
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        String englishValue = value.get(LocaleConstants.DEFAULT_LOCALE.toString());
        if (!StringUtils.isEmpty(englishValue)) {
            return true;
        }
        return false;
    }
}
