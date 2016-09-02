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

import com.fernandocejas.arrow.optional.Optional;

/**
 * @author wilqor
 */
public interface AuthUtility {
    Optional<String> getRefreshToken();

    Optional<String> getAuthToken();

    Optional<String> getLogin();

    void saveTokens(String authToken, String refreshToken, String login);

    void saveAuthToken(String authToken);

    void clearAllTokens();

    boolean isUserLoggedIn();
}
