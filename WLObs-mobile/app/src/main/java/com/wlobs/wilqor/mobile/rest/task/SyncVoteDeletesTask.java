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
import com.wlobs.wilqor.mobile.persistence.model.Vote;
import com.wlobs.wilqor.mobile.persistence.model.Vote_Table;
import com.wlobs.wilqor.mobile.rest.api.VotesService;
import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * @author wilqor
 */
final class SyncVoteDeletesTask implements RemoteTask {
    private final VotesService votesService;
    private final ErrorResponseHandler errorResponseHandler;
    private final String userLogin;

    SyncVoteDeletesTask(VotesService votesService, ErrorResponseHandler errorResponseHandler, String userLogin) {
        this.votesService = votesService;
        this.errorResponseHandler = errorResponseHandler;
        this.userLogin = userLogin;
    }

    @Override
    public void perform() throws IOException, UnauthorizedException, ErrorResponseException {
        List<Vote> deletedVotes = SQLite.select()
                .from(Vote.class)
                .where(Vote_Table.voter.eq(userLogin))
                .and(Vote_Table.deleted.eq(true))
                .queryList();
        for (Vote deleted : deletedVotes) {
            Response<Void> deleteResponse = votesService.deleteVote(userLogin, deleted.getRemoteId()).execute();
            if (!deleteResponse.isSuccessful()) {
                errorResponseHandler.handleErrorResponse(deleteResponse);
            }
            deleted.delete();
        }
    }
}
