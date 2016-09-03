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

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.rest.api.UsersService;
import com.wlobs.wilqor.mobile.rest.model.AuthTokenDto;
import com.wlobs.wilqor.mobile.rest.model.LoginAndRefreshTokenDto;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

/**
 * @author wilqor
 */
final class AuthInterceptors {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    private AuthInterceptors() {
    }

    final static class AuthorizationHeaderInterceptor implements Interceptor {
        private final AuthUtility authUtility;

        AuthorizationHeaderInterceptor(AuthUtility authUtility) {
            this.authUtility = authUtility;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Optional<String> authOptional = authUtility.getAuthToken();
            if (authOptional.isPresent()) {
                request = request.newBuilder()
                        .header(AUTH_HEADER_NAME, AUTH_HEADER_VALUE_PREFIX + authOptional.get())
                        .build();
            }
            return chain.proceed(request);
        }
    }

    final static class RefreshTokenAuthenticator implements Authenticator {
        private final UsersService usersService;
        private final AuthUtility authUtility;

        RefreshTokenAuthenticator(UsersService usersService, AuthUtility authUtility) {
            this.usersService = usersService;
            this.authUtility = authUtility;
        }

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            Request result = null;
            Optional<String> refreshOptional = authUtility.getRefreshToken();
            Optional<String> loginOptional = authUtility.getLogin();
            if (refreshOptional.isPresent() && loginOptional.isPresent()) {
                Call<AuthTokenDto> authTokenCall = usersService.refreshAuthToken(new LoginAndRefreshTokenDto(loginOptional.get(), refreshOptional.get()));
                retrofit2.Response<AuthTokenDto> dtoResponse = authTokenCall.execute();
                if (dtoResponse.isSuccessful()) {
                    String authToken = dtoResponse.body().getAuthToken();
                    String attachedHeader = AUTH_HEADER_VALUE_PREFIX + authToken;
                    if (!attachedHeader.equals(response.request().header(AUTH_HEADER_NAME))) {
                        // this means that a new auth token was obtained that should be used for subsequent calls
                        authUtility.saveAuthToken(authToken);
                        result = response.request().newBuilder()
                                .header(AUTH_HEADER_NAME, attachedHeader)
                                .build();
                    }
                }
            }
            return result;
        }
    }
}
