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

import com.wlobs.wilqor.server.auth.annotations.RequiredIdentityMatchingLogin;
import com.wlobs.wilqor.server.auth.annotations.RequiredUserOrAdminRole;
import com.wlobs.wilqor.server.persistence.model.UserStatType;
import com.wlobs.wilqor.server.rest.model.ExistingObservationDto;
import com.wlobs.wilqor.server.rest.model.NewObservationDto;
import com.wlobs.wilqor.server.rest.model.ObservationRestrictionDto;
import com.wlobs.wilqor.server.service.ObservationService;
import com.wlobs.wilqor.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/observations")
@RequiredUserOrAdminRole
public class ObservationController {
    private final ObservationService observationService;
    private final UserService userService;

    @Autowired
    public ObservationController(ObservationService observationService, UserService userService) {
        this.observationService = observationService;
        this.userService = userService;
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.POST, value = "/{login}")
    public void addObservation(@PathVariable("login") String login, @RequestBody @Valid NewObservationDto observationDto) {
        observationService.addObservation(login, observationDto);
        userService.incrementUserStat(login, UserStatType.OBSERVATIONS_COUNT);
        // increment created statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.PUT, value = "/{login}/{id}")
    public void changeObservationRestriction(@PathVariable("login") String login, @PathVariable("id") String observationId,
                                             @RequestBody ObservationRestrictionDto observationRestrictionDto) {
        observationService.updateObservationRestriction(login, observationId, observationRestrictionDto);
        // increment changed statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.DELETE, value = "/{login}/{id}")
    public void removeObservation(@PathVariable("login") String login, @PathVariable("id") String observationId) {
        observationService.removeObservation(login, observationId);
        userService.decrementUserStat(login, UserStatType.OBSERVATIONS_COUNT);
        // increment deleted statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.GET, value = "/{login}")
    public List<ExistingObservationDto> getUserObservations(@PathVariable("login") String login) {
        return observationService.getUserObservations(login);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{login}/{id}")
    public ExistingObservationDto getObservation(@PathVariable("login") String login, @PathVariable("id") String observationId) {
        String principalLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return observationService.getUserObservation(principalLogin, login, observationId);
    }
}
