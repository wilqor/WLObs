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
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Observation_Table;
import com.wlobs.wilqor.mobile.rest.api.ObservationsService;
import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * @author wilqor
 */
final class SyncObservationDeletesTask implements RemoteTask {
    private final ObservationsService observationsService;
    private final ErrorResponseHandler errorResponseHandler;
    private final String userLogin;

    SyncObservationDeletesTask(ObservationsService observationsService, ErrorResponseHandler errorResponseHandler, String userLogin) {
        this.observationsService = observationsService;
        this.errorResponseHandler = errorResponseHandler;
        this.userLogin = userLogin;
    }

    @Override
    public void perform() throws IOException, UnauthorizedException, ErrorResponseException {
        List<Observation> deletedObservations = SQLite.select()
                .from(Observation.class)
                .where(Observation_Table.author.eq(userLogin))
                .and(Observation_Table.deleted.eq(true))
                .queryList();
        for (Observation deleted : deletedObservations) {
            Response<Void> deleteResponse = observationsService.deleteObservation(userLogin, deleted.getRemoteId()).execute();
            if (!deleteResponse.isSuccessful()) {
                errorResponseHandler.handleErrorResponse(deleteResponse);
            }
            deleted.delete();
        }
    }
}
