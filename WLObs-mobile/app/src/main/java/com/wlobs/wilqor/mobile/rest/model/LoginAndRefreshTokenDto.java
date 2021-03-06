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

package com.wlobs.wilqor.mobile.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author wilqor
 */
public final class LoginAndRefreshTokenDto {
    @SerializedName("login")
    @Expose
    private final String login;

    @SerializedName("refreshToken")
    @Expose
    private final String refreshToken;

    public LoginAndRefreshTokenDto(String login, String refreshToken) {
        this.login = login;
        this.refreshToken = refreshToken;
    }

    public String getLogin() {
        return login;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "LoginAndRefreshTokenDto{" +
                "login='" + login + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
