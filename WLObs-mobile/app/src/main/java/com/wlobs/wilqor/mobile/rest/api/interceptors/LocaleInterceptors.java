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

package com.wlobs.wilqor.mobile.rest.api.interceptors;

import android.content.res.Resources;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author wilqor
 */
final class LocaleInterceptors {
    private static final String LOCALE_HEADER_NAME = "Accept-Language";

    private LocaleInterceptors() {
    }

    static final class AcceptLocaleInterceptor implements Interceptor {
        private final Locale acceptLocale;

        AcceptLocaleInterceptor(Resources resources) {
            acceptLocale = resources.getConfiguration().locale;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(
                    chain.request().newBuilder()
                            .header(LOCALE_HEADER_NAME, acceptLocale.toString())
                            .build());
        }
    }
}
