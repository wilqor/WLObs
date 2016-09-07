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

import android.content.Context;

import com.wlobs.wilqor.mobile.R;

/**
 * @author wilqor
 */
public final class InputValidators {
    public static final int MIN_LOGIN_LENGTH = 4;
    public static final int MAX_LOGIN_LENGTH = 25;
    public static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 40;

    private InputValidators() {
    }

    public static InputValidator<String> getLoginValidator(Context ctx) {
        return new CompositeInputValidator(
                new BaseInputValidator(
                        new EmptyStringErrorChecker(),
                        ctx.getString(R.string.error_field_required)
                ),
                new BaseInputValidator(
                        new TooShortStringErrorChecker(MIN_LOGIN_LENGTH),
                        ctx.getString(R.string.error_login_too_short)
                ),
                new BaseInputValidator(
                        new TooLongStringErrorChecker(MAX_LOGIN_LENGTH),
                        ctx.getString(R.string.error_login_too_long)
                ),
                new BaseInputValidator(
                        new PatternStringErrorChecker(ALPHANUMERIC_PATTERN),
                        ctx.getString(R.string.error_login_invalid)
                )
        );
    }

    public static InputValidator<String> getPasswordValidator(Context ctx) {
        return new CompositeInputValidator(
                new BaseInputValidator(
                        new EmptyStringErrorChecker(),
                        ctx.getString(R.string.error_field_required)
                ),
                new BaseInputValidator(
                        new TooShortStringErrorChecker(MIN_PASSWORD_LENGTH),
                        ctx.getString(R.string.error_password_too_short)
                ),
                new BaseInputValidator(
                        new TooLongStringErrorChecker(MAX_PASSWORD_LENGTH),
                        ctx.getString(R.string.error_password_too_long)
                )
        );
    }
}
