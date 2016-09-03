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

import android.content.Context;
import android.content.res.Resources;

import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.rest.api.interceptors.Interceptors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wilqor
 */
public final class RestServices {
    private RestServices() {
    }

    public static UsersService getUsersService(Context ctx) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl(ctx))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UsersService.class);
    }

    public static AggregationService getAggregationService(Context ctx,
                                                           AuthUtility authUtility) {
        Retrofit retrofit = getRetrofitWithClient(ctx, getHttpClientWithAuthentication(ctx, authUtility));
        return retrofit.create(AggregationService.class);
    }

    public static ObservationsService getObservationsService(Context ctx,
                                                             AuthUtility authUtility,
                                                             Resources resources) {
        Retrofit retrofit = getRetrofitWithClient(ctx, getHttpClientWithAuthenticationAndLocale(ctx, authUtility, resources));
        return retrofit.create(ObservationsService.class);
    }

    public static SpeciesService getSpeciesService(Context ctx,
                                                   AuthUtility authUtility,
                                                   Resources resources) {
        Retrofit retrofit = getRetrofitWithClient(ctx, getHttpClientWithAuthenticationAndLocale(ctx, authUtility, resources));
        return retrofit.create(SpeciesService.class);
    }

    public static VotesService getVotesService(Context ctx,
                                               AuthUtility authUtility,
                                               Resources resources) {
        Retrofit retrofit = getRetrofitWithClient(ctx, getHttpClientWithAuthenticationAndLocale(ctx, authUtility, resources));
        return retrofit.create(VotesService.class);
    }

    private static OkHttpClient getHttpClientWithAuthentication(Context ctx, AuthUtility authUtility) {
        return new OkHttpClient.Builder()
                .authenticator(Interceptors.getRefreshTokenAuthenticator(getUsersService(ctx), authUtility))
                .addInterceptor(Interceptors.getAuthorizationHeaderInterceptor(authUtility))
                .build();
    }

    private static OkHttpClient getHttpClientWithAuthenticationAndLocale(Context ctx,
                                                                         AuthUtility authUtility,
                                                                         Resources resources) {
        return new OkHttpClient.Builder()
                .authenticator(Interceptors.getRefreshTokenAuthenticator(getUsersService(ctx), authUtility))
                .addInterceptor(Interceptors.getAuthorizationHeaderInterceptor(authUtility))
                .addInterceptor(Interceptors.getAcceptLocaleInterceptor(resources))
                .build();
    }

    private static Retrofit getRetrofitWithClient(Context ctx,
                                                  OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl(ctx))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static String getBaseUrl(Context ctx) {
        return ctx.getString(R.string.server_api_path);
    }
}
