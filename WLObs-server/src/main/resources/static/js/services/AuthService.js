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
                        TokenService.saveTokens(authToken, refreshToken);
                    } else {
                        return $q.reject('Insufficient permissions, only admins allowed.');
                    }
                }, function () {
                    return $q.reject('Invalid login or password, try again.')
                });
        },
        logOut: function () {
            TokenService.clearTokens();
            $location.path('/login');
        }
    };
    return AuthService;
});