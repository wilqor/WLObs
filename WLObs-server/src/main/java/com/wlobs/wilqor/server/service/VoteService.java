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

import com.wlobs.wilqor.server.rest.model.ExistingVoteDto;
import com.wlobs.wilqor.server.rest.model.NewVoteDto;

import java.util.List;

/**
 * @author wilqor
 */
public interface VoteService {
    ExistingVoteDto castAndReturnVote(String login, NewVoteDto newVoteDto);

    ExistingVoteDto removeAndReturnVote(String login, String voteId);

    List<ExistingVoteDto> getUserVotes(String login);
}