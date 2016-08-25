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
import com.wlobs.wilqor.server.rest.model.ExistingVoteDto;
import com.wlobs.wilqor.server.rest.model.NewVoteDto;
import com.wlobs.wilqor.server.service.ObservationService;
import com.wlobs.wilqor.server.service.UserService;
import com.wlobs.wilqor.server.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/votes")
@RequiredUserOrAdminRole
public class VoteController {
    private final VoteService voteService;
    private final UserService userService;
    private final ObservationService observationService;

    @Autowired
    public VoteController(VoteService voteService, UserService userService, ObservationService observationService) {
        this.voteService = voteService;
        this.userService = userService;
        this.observationService = observationService;
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.POST, value = "/{login}")
    public void castVote(@PathVariable("login") String login, @RequestBody @Valid NewVoteDto newVoteDto) {
        ExistingVoteDto voteDto = voteService.castAndReturnVote(login, newVoteDto);
        observationService.incrementObservationVotesCount(voteDto.getObservationId());
        userService.incrementUserStat(voteDto.getVoter(), UserStatType.VOTES_CASTED);
        userService.incrementUserStat(voteDto.getObservationOwner(), UserStatType.VOTES_RECEIVED);
        // increment casted votes statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.DELETE, value = "/{login}/{vote_id}")
    public void removeVote(@PathVariable("login") String login, @PathVariable("vote_id") String voteId) {
        ExistingVoteDto voteDto = voteService.removeAndReturnVote(login, voteId);
        observationService.decrementObservationVotesCount(voteDto.getObservationId());
        userService.decrementUserStat(voteDto.getVoter(), UserStatType.VOTES_CASTED);
        userService.decrementUserStat(voteDto.getObservationOwner(), UserStatType.VOTES_RECEIVED);
        // increment removed votes statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.GET, value = "/{login}")
    public List<ExistingVoteDto> getUserVotes(@PathVariable("login") String login) {
        return voteService.getUserVotes(login);
    }
}
