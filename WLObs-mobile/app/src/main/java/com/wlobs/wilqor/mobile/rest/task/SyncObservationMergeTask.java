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
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.rest.api.ObservationsService;
import com.wlobs.wilqor.mobile.rest.model.ExistingObservationDto;
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
final class SyncObservationMergeTask implements RemoteTask {
    private final ObservationsService observationsService;
    private final ErrorResponseHandler errorResponseHandler;
    private final String userLogin;

    public SyncObservationMergeTask(ObservationsService observationsService, ErrorResponseHandler errorResponseHandler, String userLogin) {
        this.observationsService = observationsService;
        this.errorResponseHandler = errorResponseHandler;
        this.userLogin = userLogin;
    }

    @Override
    public void perform() throws IOException, UnauthorizedException, ErrorResponseException {
        Response<List<ExistingObservationDto>> getResponse = observationsService.getObservations(userLogin).execute();
        if (getResponse.isSuccessful()) {
            List<ExistingObservationDto> remoteDtos = getResponse.body();
            Map<String, Observation> convertedMappedById = new HashMap<>();
            for (ExistingObservationDto dto : remoteDtos) {
                convertedMappedById.put(dto.getId(), fromRemote(dto));
            }
            List<Observation> localObservations = SQLite.select()
                    .from(Observation.class)
                    .where(Observation_Table.author.eq(userLogin))
                    .queryList();
            mergeObservations(convertedMappedById, localObservations);
        } else {
            errorResponseHandler.handleErrorResponse(getResponse);
        }
    }

    private Observation fromRemote(ExistingObservationDto dto) {
        Observation observation = new Observation();
        observation.setAuthor(dto.getAuthor());
        observation.setDateUtcTimestamp(dto.getDateUtcTimestamp());
        observation.setRemoteId(dto.getId());
        observation.setRestricted(dto.isRestricted());
        observation.setVotesCount(dto.getVotesCount());
        observation.setLongitude(dto.getLongitude());
        observation.setLatitude(dto.getLatitude());
        observation.setSpecies(SQLite.select()
                .from(Species.class)
                .where(Species_Table.speciesClass.eq(dto.getSpeciesStub().getSpeciesClass()))
                .and(Species_Table.latinName.eq(dto.getSpeciesStub().getLatinName()))
                .querySingle());
        return observation;
    }

    private void mergeObservations(Map<String, Observation> convertedMappedById, List<Observation> localObservations) {
        // merge observations present in both collections
        for (Observation local : localObservations) {
            String localRemoteId = local.getRemoteId();
            if (!local.isRemoteItNotSet() && convertedMappedById.containsKey(localRemoteId)) {
                // the only mutable values are restriction and votes count
                Observation matchingRemote = convertedMappedById.get(localRemoteId);
                local.setVotesCount(matchingRemote.getVotesCount());
                local.setRestricted(matchingRemote.isRestricted());
                local.update();
                // already taken into account, remove from map
                convertedMappedById.remove(localRemoteId);
            } else {
                // not found on the server side, delete it locally
                local.delete();
            }
        }
        // add new observations found in the remote set
        for (Observation newRemote : convertedMappedById.values()) {
            newRemote.save();
        }
    }
}
