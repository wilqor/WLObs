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
import com.wlobs.wilqor.server.rest.model.ExistingVoteDto;
import com.wlobs.wilqor.server.rest.model.NewVoteDto;
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

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.POST, value = "/{login}")
    public void castVote(@PathVariable("login") String login, @RequestBody @Valid NewVoteDto newVoteDto) {
        voteService.castVote(login, newVoteDto);

        // inc observation votes
        // inc owner received votes
        // inc voter casted votes
        // increment casted votes statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.DELETE, value = "/{login}/{vote_id}")
    public void removeVote(@PathVariable("login") String login, @PathVariable("vote_id") String voteId) {
        voteService.removeVote(login, voteId);

        // dec observation votes
        // dec owner received votes
        // dec voter casted votes
        // increment removed votes statistics
    }

    @RequiredIdentityMatchingLogin
    @RequestMapping(method = RequestMethod.GET, value = "/{login}")
    public List<ExistingVoteDto> getUserVotes(@PathVariable("login") String login) {
        return voteService.getUserVotes(login);
    }
}
