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
app.factory('AuthService', function ($http, $location, TokenService, $q) {
    var AuthService = {
        isLoggedIn: function () {
            return TokenService.getAuthToken() != undefined;
        },
        logIn: function (login, password) {
            return $http.post('/auth/authorize', {login: login, password: password})
                .then(function (response) {
                    var authToken = response.data.authToken;
                    var refreshToken = response.data.refreshToken;
                    if (TokenService.hasAdminRole(authToken)) {
                        TokenService.saveTokensAndLogin(authToken, refreshToken, login);
                    } else {
                        return $q.reject('Insufficient permissions, only admins allowed.');
                    }
                }, function () {
                    return $q.reject('Invalid login or password, try again.')
                });
        },
        refreshAuthToken: function () {
            return $http.post('/auth/refresh', {
                login: TokenService.getLogin(),
                refreshToken: TokenService.getRefreshToken()
            })
                .then(function (response) {
                    TokenService.saveAuthToken(response.data.authToken);
                    return TokenService.getAuthToken();
                }, function () {
                    return $q.reject();
                });
        },
        logOut: function () {
            TokenService.clearTokens();
            $location.path('/login');
        }
    };
    return AuthService;
});