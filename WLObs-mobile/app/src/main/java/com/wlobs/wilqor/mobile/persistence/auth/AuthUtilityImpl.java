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

package com.wlobs.wilqor.mobile.persistence.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fernandocejas.arrow.optional.Optional;

/**
 * @author wilqor
 */
final class AuthUtilityImpl implements AuthUtility {
    private static final String AUTH_TOKEN_KEY = "AUTH_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";
    private static final String LOGIN = "LOGIN";

    private final SharedPreferences preferences;

    AuthUtilityImpl(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Optional<String> getRefreshToken() {
        return Optional.fromNullable(preferences.getString(REFRESH_TOKEN_KEY, null));
    }

    @Override
    public Optional<String> getAuthToken() {
        return Optional.fromNullable(preferences.getString(AUTH_TOKEN_KEY, null));
    }

    @Override
    public Optional<String> getLogin() {
        return Optional.fromNullable(preferences.getString(LOGIN, null));
    }

    @Override
    public void saveTokens(String authToken, String refreshToken, String login) {
        saveAuthToken(authToken);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.putString(LOGIN, login);
        editor.apply();
    }

    @Override
    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    @Override
    public boolean isUserLoggedIn() {
        Optional<String> authToken = getAuthToken();
        Optional<String> refreshToken = getRefreshToken();
        Optional<String> login = getLogin();
        return authToken.isPresent() && refreshToken.isPresent() && login.isPresent();
    }

    @Override
    public void clearAllTokens() {
        saveTokens(null, null, null);
    }
}
