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

package com.wlobs.wilqor.server.rest;

import com.wlobs.wilqor.server.persistence.model.StatMetaData;
import com.wlobs.wilqor.server.rest.model.AuthAndRefreshTokensDto;
import com.wlobs.wilqor.server.rest.model.AuthTokenDto;
import com.wlobs.wilqor.server.rest.model.CredentialsDto;
import com.wlobs.wilqor.server.rest.model.LoginAndRefreshTokenDto;
import com.wlobs.wilqor.server.service.StatsService;
import com.wlobs.wilqor.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final StatsService statsService;

    @Autowired
    public AuthController(UserService userService, StatsService statsService) {
        this.userService = userService;
        this.statsService = statsService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authorize")
    public AuthAndRefreshTokensDto authorize(@RequestBody @Valid final CredentialsDto credentialsDto) {
        AuthAndRefreshTokensDto tokensDto = userService.authorize(credentialsDto);
        statsService.incrementOperationStats(StatMetaData.StatOperation.USER_AUTHORIZATION);
        return tokensDto;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/refresh")
    public AuthTokenDto refreshToken(@RequestBody @Valid final LoginAndRefreshTokenDto loginAndRefreshTokenDto) {
        return userService.refreshToken(loginAndRefreshTokenDto);
    }
}
