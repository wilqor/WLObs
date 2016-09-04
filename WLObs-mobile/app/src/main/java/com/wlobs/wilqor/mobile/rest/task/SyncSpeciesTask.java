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

import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.rest.api.SpeciesService;
import com.wlobs.wilqor.mobile.rest.model.SpeciesCountDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;
import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * @author wilqor
 */
final class SyncSpeciesTask implements RemoteTask {
    private final SpeciesService speciesService;
    private final ErrorResponseHandler errorResponseHandler;

    SyncSpeciesTask(SpeciesService speciesService, ErrorResponseHandler errorResponseHandler) {
        this.speciesService = speciesService;
        this.errorResponseHandler = errorResponseHandler;
    }

    @Override
    public void perform() throws IOException, UnauthorizedException, ErrorResponseException {
        Response<SpeciesCountDto> speciesCountResponse = speciesService.getSpeciesCount().execute();
        if (speciesCountResponse.isSuccessful()) {
            SpeciesCountDto speciesCountDto = speciesCountResponse.body();
            handleSpeciesCountDto(speciesCountDto);
        }  else {
            errorResponseHandler.handleErrorResponse(speciesCountResponse);
        }
    }

    private void handleSpeciesCountDto(SpeciesCountDto speciesCountDto) throws IOException, UnauthorizedException, ErrorResponseException {
        for (Map.Entry<SpeciesStub.Class, Long> entry : speciesCountDto.getSpeciesCount().entrySet()) {
            SpeciesStub.Class currentClass = entry.getKey();
            Long remoteCount = entry.getValue();
            long localCount = SQLite.select(Method.count())
                    .from(Species.class)
                    .where(Species_Table.speciesClass.eq(currentClass))
                    .count();
            if (localCount < remoteCount) {
                Response<List<SpeciesDto>> classResponse = speciesService.getSpeciesForClass(currentClass).execute();
                if (classResponse.isSuccessful()) {
                    List<SpeciesDto> speciesDtos = classResponse.body();
                    handleSpeciesDtos(speciesDtos);
                } else {
                    errorResponseHandler.handleErrorResponse(classResponse);
                }
            }
        }
    }

    private void handleSpeciesDtos(List<SpeciesDto> speciesDtos) {
        for (SpeciesDto remoteDto : speciesDtos) {
             Species record = SQLite.select()
                    .from(Species.class)
                    .where(Species_Table.speciesClass.eq(remoteDto.getSpeciesClass()))
                    .and(Species_Table.latinName.eq(remoteDto.getLatinName()))
                    .querySingle();
            if (record == null) {
                record = new Species(remoteDto);
                record.save();
            } else {
                record.setName(remoteDto.getName());
                record.update();
            }
        }
    }
}
