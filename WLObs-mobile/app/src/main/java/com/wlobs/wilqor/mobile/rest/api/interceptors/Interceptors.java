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

import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.rest.api.UsersService;

import okhttp3.Authenticator;
import okhttp3.Interceptor;

/**
 * @author wilqor
 */
public final class Interceptors {
    private Interceptors() {
    }

    public static Interceptor getAuthorizationHeaderInterceptor(AuthUtility authUtility) {
        return new AuthInterceptors.AuthorizationHeaderInterceptor(authUtility);
    }

    public static Authenticator getRefreshTokenAuthenticator(UsersService usersService, AuthUtility authUtility) {
        return new AuthInterceptors.RefreshTokenAuthenticator(usersService, authUtility);
    }

    public static Interceptor getAcceptLocaleInterceptor(Resources resources) {
        return new LocaleInterceptors.AcceptLocaleInterceptor(resources);
    }
}
