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

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.rest.api.interceptors.Interceptors;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
        Retrofit retrofit = getRetrofitWithClient(ctx, getHttpClientBase(ctx));
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

    private static OkHttpClient getHttpClientBase(Context ctx) {
        Optional<RestSecurityConfig> securityConfig = getSecurityConfig(ctx);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (securityConfig.isPresent()) {
            RestSecurityConfig restSecurityConfig = securityConfig.get();
            builder = builder.sslSocketFactory(restSecurityConfig.getSslSocketFactory(), restSecurityConfig.getTrustManager())
                    .hostnameVerifier(new PermissiveHostNameVerifier());
        }
        return builder
                .build();
    }

    private static OkHttpClient getHttpClientWithAuthentication(Context ctx, AuthUtility authUtility) {
        Optional<RestSecurityConfig> securityConfig = getSecurityConfig(ctx);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (securityConfig.isPresent()) {
            RestSecurityConfig restSecurityConfig = securityConfig.get();
            builder = builder.sslSocketFactory(restSecurityConfig.getSslSocketFactory(), restSecurityConfig.getTrustManager())
                    .hostnameVerifier(new PermissiveHostNameVerifier());
        }
        return builder
                .authenticator(Interceptors.getRefreshTokenAuthenticator(getUsersService(ctx), authUtility))
                .addInterceptor(Interceptors.getAuthorizationHeaderInterceptor(authUtility))
                .build();
    }

    private static OkHttpClient getHttpClientWithAuthenticationAndLocale(Context ctx,
                                                                         AuthUtility authUtility,
                                                                         Resources resources) {
        Optional<RestSecurityConfig> securityConfig = getSecurityConfig(ctx);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (securityConfig.isPresent()) {
            RestSecurityConfig restSecurityConfig = securityConfig.get();
            builder = builder.sslSocketFactory(restSecurityConfig.getSslSocketFactory(), restSecurityConfig.getTrustManager())
                    .hostnameVerifier(new PermissiveHostNameVerifier());
        }
        return builder
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

    // source: http://stackoverflow.com/questions/29273387/certpathvalidatorexception-trust-anchor-for-certificate-path-not-found-retro/31436459#31436459
    private static Optional<RestSecurityConfig> getSecurityConfig(Context ctx) {
        Optional<RestSecurityConfig> securityConfigOptional = Optional.absent();
        try {
            // loading CA
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca;
            InputStream certInput = ctx.getResources().openRawResource(R.raw.wlobs_server_certificate);
            try {
                ca = cf.generateCertificate(certInput);
            } finally {
                certInput.close();
            }
            // creating KeyStore containing the CA
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            // creating TrustManager that trusts CA specified in KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            // SSLSocketFactory using TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            securityConfigOptional = Optional.of(new RestSecurityConfig(sslContext.getSocketFactory(), trustManager));
        } catch (Exception ignored) {
        }
        return securityConfigOptional;
    }

    private static final class RestSecurityConfig {
        private final SSLSocketFactory sslSocketFactory;
        private final X509TrustManager trustManager;

        private RestSecurityConfig(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
            this.sslSocketFactory = sslSocketFactory;
            this.trustManager = trustManager;
        }

        public SSLSocketFactory getSslSocketFactory() {
            return sslSocketFactory;
        }

        public X509TrustManager getTrustManager() {
            return trustManager;
        }

        @Override
        public String toString() {
            return "HttpSecurityConfig{" +
                    "sslSocketFactory=" + sslSocketFactory +
                    ", trustManager=" + trustManager +
                    '}';
        }
    }

    private static final class PermissiveHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
