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

package com.wlobs.wilqor.server.config;

import com.wlobs.wilqor.server.auth.CustomAuthenticationEntryPoint;
import com.wlobs.wilqor.server.auth.JwtAuthenticationExtractorImpl;
import com.wlobs.wilqor.server.auth.OuterAuthenticationExtractor;
import com.wlobs.wilqor.server.auth.TokenAuthenticationAttachingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author wilqor
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // allow anonymous requests to specified paths
                .antMatchers(
                        HttpMethod.GET,
                        "/health"
                ).permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(tokenAuthenticationAttachingFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
    }

    private AuthenticationEntryPoint authEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    private TokenAuthenticationAttachingFilter tokenAuthenticationAttachingFilter() {
        return new TokenAuthenticationAttachingFilter(new OuterAuthenticationExtractor(
                new JwtAuthenticationExtractorImpl()
        ));
    }
}
