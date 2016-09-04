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

import com.wlobs.wilqor.mobile.rest.api.ObservationsService;
import com.wlobs.wilqor.mobile.rest.api.SpeciesService;
import com.wlobs.wilqor.mobile.rest.api.VotesService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wilqor
 */
public final class RemoteTasks {
    private RemoteTasks() {
    }

    public static List<RemoteTask> getSyncTasks(
            SpeciesService speciesService,
            ObservationsService observationsService,
            VotesService votesService,
            String userLogin) {
        ErrorResponseHandler unauthorizedErrorHandler = new UnauthorizedErrorResponseHandler();
        ErrorResponseHandler combinedErrorHandler = new DefaultErrorResponseHandler(
                unauthorizedErrorHandler
        );
        List<RemoteTask> tasks = new ArrayList<>();
        tasks.add(new SyncSpeciesTask(speciesService, combinedErrorHandler));
        tasks.add(new SyncObservationDeletesTask(observationsService, unauthorizedErrorHandler, userLogin));
        tasks.add(new SyncObservationCreatesTask(observationsService, unauthorizedErrorHandler, userLogin));
        tasks.add(new SyncObservationRestrictionUpdatesTask(observationsService, unauthorizedErrorHandler, userLogin));
        tasks.add(new SyncObservationMergeTask(observationsService, combinedErrorHandler, userLogin));
        tasks.add(new SyncVoteDeletesTask(votesService, unauthorizedErrorHandler, userLogin));
        tasks.add(new SyncVoteCreatesTask(votesService, unauthorizedErrorHandler, userLogin));
        tasks.add(new SyncVoteMergeTask(votesService, combinedErrorHandler, userLogin));
        return tasks;
    }
}
