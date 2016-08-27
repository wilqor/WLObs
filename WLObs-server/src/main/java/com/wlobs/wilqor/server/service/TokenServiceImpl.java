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

package com.wlobs.wilqor.server.service;

import com.wlobs.wilqor.server.config.AuthConstants;
import com.wlobs.wilqor.server.persistence.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wilqor
 */
@Service
public class TokenServiceImpl implements TokenService {
    private final String base64SecretKey;

    @Autowired
    public TokenServiceImpl(@Value("${jwt.secret}") String base64SecretKey) {
        this.base64SecretKey = base64SecretKey;
    }

    @Override
    public Optional<Authentication> extract(String content) {
        Optional<Authentication> result = Optional.empty();
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(base64SecretKey)
                    .parseClaimsJws(content)
                    .getBody();
            String userName = body.getSubject();
            String roles = body.get(AuthConstants.JWT_ROLES_KEY, String.class);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
            result = Optional.of(new UsernamePasswordAuthenticationToken(userName, userName, authorities));
        } catch (JwtException ignored) {
            // just do not set result
        }
        return result;
    }

    @Override
    public String buildAuthToken(String login, List<User.Role> roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + AuthConstants.JWT_EXPIRATION_PERIOD_IN_MILLIS);
        return Jwts.builder()
                .claim(AuthConstants.JWT_ROLES_KEY, roles
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(AuthConstants.JWT_ROLES_SEPARATOR)))
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, base64SecretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
