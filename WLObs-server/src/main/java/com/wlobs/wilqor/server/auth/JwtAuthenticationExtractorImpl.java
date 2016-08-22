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

package com.wlobs.wilqor.server.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author wilqor
 */
public class JwtAuthenticationExtractorImpl implements AuthenticationExtractor {
    private static final String SECRET_IN_BASE64 = "SECRET_IN_BASE64";
    private static final String ROLES_KEY = "roles";

    @Override
    public Optional<Authentication> extract(String content) {
        Optional<Authentication> result = Optional.empty();
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET_IN_BASE64)
                    .parseClaimsJws(content)
                    .getBody();
            String userName = body.getSubject();
            String roles = body.get(ROLES_KEY, String.class);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
            result = Optional.of(new UsernamePasswordAuthenticationToken(userName, userName, authorities));
        } catch (JwtException ignored) {
            // just do not set result
        }
        return result;
    }
}
