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

import com.wlobs.wilqor.server.rest.model.AuthAndRefreshTokensDto;
import com.wlobs.wilqor.server.rest.model.CredentialsDto;
import com.wlobs.wilqor.server.rest.model.ResetPasswordDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @RequestMapping(method = RequestMethod.POST)
    public AuthAndRefreshTokensDto register(@RequestBody @Valid final CredentialsDto credentialsDto) {
        return new AuthAndRefreshTokensDto(
                "some auth token",
                "some refresh token"
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{login}/resetPassword")
    public AuthAndRefreshTokensDto resetPassword(@PathVariable("login") String login, @RequestBody @Valid final ResetPasswordDto resetPasswordDto) {
        return new AuthAndRefreshTokensDto(
                "some auth token with login: " + login,
                "some refresh token"
        );
    }
}
