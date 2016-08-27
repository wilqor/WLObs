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

import com.wlobs.wilqor.server.auth.annotations.RequiredAdminRole;
import com.wlobs.wilqor.server.persistence.model.StatMetaData;
import com.wlobs.wilqor.server.rest.model.*;
import com.wlobs.wilqor.server.service.StatsService;
import com.wlobs.wilqor.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final StatsService statsService;

    @Autowired
    public UserController(UserService userService, StatsService statsService) {
        this.userService = userService;
        this.statsService = statsService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public AuthAndRefreshTokensDto register(@RequestBody @Valid final CredentialsDto credentialsDto) {
        AuthAndRefreshTokensDto tokensDto = userService.register(credentialsDto);
        statsService.incrementOperationStats(StatMetaData.StatOperation.USER_REGISTRATION);
        statsService.incrementOperationStats(StatMetaData.StatOperation.USER_AUTHORIZATION);
        return tokensDto;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{login}/resetPassword")
    public AuthAndRefreshTokensDto resetPassword(@PathVariable("login") String login, @RequestBody @Valid final ResetPasswordDto resetPasswordDto) {
        AuthAndRefreshTokensDto tokensDto = userService.resetPassword(login, resetPasswordDto);
        statsService.incrementOperationStats(StatMetaData.StatOperation.USER_PASSWORD_CHANGE);
        return tokensDto;
    }

    @RequestMapping(method = RequestMethod.GET)
    @RequiredAdminRole
    public RecordsPageDto<FlatUserDto> getUsersPage(@RequestParam(value = "pageNo") Optional<Integer> pageNumber,
                                                    @RequestParam(value = "sortBy") Optional<SortParameters.UserSort> sort,
                                                    @RequestParam(value = "direction") Optional<Sort.Direction> direction) {
        int requestPageNumber = pageNumber.orElse(RecordsPageDto.DEFAULT_PAGE_NUMBER);
        SortParameters.UserSort requestSort = sort.orElse(SortParameters.UserSort.LOGIN);
        Sort.Direction requestDirection = direction.orElse(Sort.Direction.ASC);
        return userService.getUsersPage(requestPageNumber, requestSort.asParameter(), requestDirection);
    }
}
