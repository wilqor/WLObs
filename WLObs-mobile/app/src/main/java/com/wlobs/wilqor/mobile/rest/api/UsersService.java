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

package com.wlobs.wilqor.mobile.rest.api;

import com.wlobs.wilqor.mobile.rest.model.AuthAndRefreshTokensDto;
import com.wlobs.wilqor.mobile.rest.model.AuthTokenDto;
import com.wlobs.wilqor.mobile.rest.model.CredentialsDto;
import com.wlobs.wilqor.mobile.rest.model.LoginAndRefreshTokenDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author wilqor
 */
public interface UsersService {
    @POST("users")
    Call<AuthAndRefreshTokensDto> register(@Body CredentialsDto credentialsDto);

    @POST("auth/authorize")
    Call<AuthAndRefreshTokensDto> login(@Body CredentialsDto credentialsDto);

    @POST("auth/refresh")
    Call<AuthTokenDto> refreshAuthToken(@Body LoginAndRefreshTokenDto loginAndRefreshTokenDto);
}
