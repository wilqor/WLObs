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

/**
 * @author wilqor
 */
app.factory('TokenService', function ($localStorage, jwtHelper) {
    var TokenService = {
        saveTokensAndLogin: function (authToken, refreshToken, login) {
            TokenService.saveAuthToken(authToken);
            TokenService.saveRefreshToken(refreshToken);
            TokenService.saveLogin(login);
        },
        saveAuthToken: function (token) {
            $localStorage.authToken = token;
        },
        saveRefreshToken: function (token) {
            $localStorage.refreshToken = token;
        },
        saveLogin: function (login) {
            $localStorage.login = login;
        },
        clearTokens: function () {
            delete $localStorage.authToken;
            delete $localStorage.refreshToken;
            delete $localStorage.login;
        },
        getAuthToken: function () {
            return $localStorage.authToken;
        },
        getRefreshToken: function () {
            return $localStorage.refreshToken;
        },
        getLogin: function () {
            return $localStorage.login;
        },
        hasAdminRole: function (authToken) {
            var roles = jwtHelper.decodeToken(authToken).roles;
            return roles.indexOf('ROLE_ADMIN') > 1;
        }
    };
    return TokenService;
});
