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

package com.wlobs.wilqor.mobile.rest.task;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.persistence.model.Vote;
import com.wlobs.wilqor.mobile.persistence.model.Vote_Table;
import com.wlobs.wilqor.mobile.rest.api.VotesService;
import com.wlobs.wilqor.mobile.rest.model.ExistingVoteDto;
import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * @author wilqor
 */
final class SyncVoteMergeTask implements RemoteTask {
    private final VotesService votesService;
    private final ErrorResponseHandler errorResponseHandler;
    private final String userLogin;

    SyncVoteMergeTask(VotesService votesService, ErrorResponseHandler errorResponseHandler, String userLogin) {
        this.votesService = votesService;
        this.errorResponseHandler = errorResponseHandler;
        this.userLogin = userLogin;
    }

    @Override
    public void perform() throws IOException, UnauthorizedException, ErrorResponseException {
        Response<List<ExistingVoteDto>> getResponse = votesService.getVotes(userLogin).execute();
        if (getResponse.isSuccessful()) {
            List<ExistingVoteDto> remoteDtos = getResponse.body();
            Map<String, Vote> convertedMappedById = new HashMap<>();
            for (ExistingVoteDto dto : remoteDtos) {
                convertedMappedById.put(dto.getId(), fromRemote(dto));
            }
            List<Vote> localVotes = SQLite.select()
                    .from(Vote.class)
                    .where(Vote_Table.voter.eq(userLogin))
                    .queryList();
            mergeVotes(convertedMappedById, localVotes);
        } else {
            errorResponseHandler.handleErrorResponse(getResponse);
        }
    }

    private Vote fromRemote(ExistingVoteDto dto) {
        Vote vote = new Vote();
        vote.setRemoteId(dto.getId());
        vote.setVoter(dto.getVoter());
        vote.setRemoteObservationId(dto.getObservationId());
        vote.setObservationOwner(dto.getObservationOwner());
        vote.setDateUtcTimestamp(dto.getDateUtcTimestamp());
        vote.setSpecies(SQLite.select()
                .from(Species.class)
                .where(Species_Table.speciesClass.eq(dto.getSpeciesStub().getSpeciesClass()))
                .and(Species_Table.latinName.eq(dto.getSpeciesStub().getLatinName()))
                .querySingle());
        return vote;
    }

    private void mergeVotes(Map<String, Vote> convertedMappedById, List<Vote> localVotes) {
        // delete votes not present in remote collection
        for (Vote local : localVotes) {
            String localRemoteId = local.getRemoteId();
            if (!local.isRemoteIdNotSet() && convertedMappedById.containsKey(localRemoteId)) {
                convertedMappedById.remove(localRemoteId);
            } else {
                local.delete();
            }
        }
        // add new votes in the remote set
        for (Vote remote : convertedMappedById.values()) {
            remote.save();
        }
    }
}
