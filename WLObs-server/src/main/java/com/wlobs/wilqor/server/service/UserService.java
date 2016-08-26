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

package com.wlobs.wilqor.server.service;

import com.wlobs.wilqor.server.persistence.repository.UserStatsModifier;
import com.wlobs.wilqor.server.rest.model.*;
import org.springframework.data.domain.Sort;

/**
 * @author wilqor
 */
public interface UserService extends UserStatsModifier {
    AuthAndRefreshTokensDto register(final CredentialsDto credentialsDto);

    AuthAndRefreshTokensDto resetPassword(String login, final ResetPasswordDto resetPasswordDto);

    AuthAndRefreshTokensDto authorize(final CredentialsDto credentialsDto);

    AuthTokenDto refreshToken(final LoginAndRefreshTokenDto loginAndRefreshTokenDto);

    RecordsPageDto<FlatUserDto> getUsersPage(int pageNumber, String sort, Sort.Direction direction);
}
