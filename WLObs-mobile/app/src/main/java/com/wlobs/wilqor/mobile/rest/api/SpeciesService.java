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

import com.wlobs.wilqor.mobile.rest.model.SpeciesCountDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author wilqor
 */
public interface SpeciesService {
    @GET("species/{speciesClass}")
    Call<List<SpeciesDto>> getSpeciesForClass(@Path("speciesClass") SpeciesStub.Class speciesClass);

    @GET("species/count")
    Call<SpeciesCountDto> getSpeciesCount();
}
