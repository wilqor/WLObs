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

package com.wlobs.wilqor.mobile.rest.api;

import com.wlobs.wilqor.mobile.rest.model.ExistingObservationDto;
import com.wlobs.wilqor.mobile.rest.model.NewObservationDto;
import com.wlobs.wilqor.mobile.rest.model.ObservationRestrictionDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author wilqor
 */
public interface ObservationsService {
    @POST("observations/{login}")
    Call<Void> addObservation(@Path("login") String login, @Body NewObservationDto observationDto);

    @PUT("observations/{login}/{observationId}")
    Call<Void> updateObservationRestriction(@Path("login") String login,
                                            @Path("observationId") String observationId,
                                            @Body ObservationRestrictionDto restrictionDto);

    @DELETE("observations/{login}/{observationId}")
    Call<Void> deleteObservation(@Path("login") String login, @Path("observationId") String observationId);

    @GET("observations")
    Call<List<ExistingObservationDto>> getObservations(@Path("login") String login);

    @GET("observations/{author}/{observationId}")
    Call<ExistingObservationDto> getObservation(@Path("author") String author, @Path("observationId") String observationId);
}
