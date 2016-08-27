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

import com.wlobs.wilqor.server.rest.exception.InvalidMonthException;
import org.springframework.stereotype.Component;

/**
 * @author wilqor
 */
@Component
public class MonthValidatorImpl implements MonthValidator {
    private static final int MIN_MONTH = 1, MAX_MONTH = 12;

    @Override
    public void validateMonth(int monthNumber) {
        if (monthNumber < MIN_MONTH || monthNumber > MAX_MONTH) {
            throw new InvalidMonthException(monthNumber);
        }
    }
}
