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

package com.wlobs.wilqor.server.rest.playground;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wilqor
 */
public class JwtTest {
    @Test
    public void testJwt() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(12));

        // !!! signing key has to be BASE64-encoded
        String secret = "secret";
        String builtJwt = Jwts.builder()
                .claim("userId", "123")
                .claim("roles", "ROLE_ADMIN")
                .setSubject("user_name")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(builtJwt)
                .getBody();

    }

    @Test
    public void testAuthoritiesFromString() {
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN, ROLE_USER");
        System.out.println(authorityList);
    }

    @Test
    public void testHashing() {
        PasswordEncoder coder = new BCryptPasswordEncoder();
        String pass = "abc def";
        String storedInDb = coder.encode(pass);
        String onceMore = "abc def";
        String another = "abcdef";

        Assert.assertTrue(coder.matches(onceMore, storedInDb));
        Assert.assertFalse(coder.matches(another, storedInDb));
    }

    @Test
    public void generate() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(12));
        String secret = "SECRET_IN_BASE64";
        String builtJwt = Jwts.builder()
                .claim("roles", "ROLE_ADMIN")
                .setSubject("user_name")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        System.out.println(builtJwt);
    }

    @Test
    public void geoHashTest() {
        System.out.println(new Date().getTime());

        // that's the way to calculate GeoHash for a new point!
        String leftBottom = GeoHash.geoHashStringWithCharacterPrecision(53.396, 16.716, 12);
        String topRight = GeoHash.geoHashStringWithCharacterPrecision(54.435, 19.210, 12);
        // now the common prefix

        System.out.println("left bottom: " + leftBottom);
        System.out.println("top right: " + topRight);
        System.out.println("Common prefix length: " + calculateCommonPrefixLength(leftBottom, topRight));

        GeoHash hash = GeoHash.fromGeohashString(leftBottom);
        WGS84Point centerPoint = hash.getBoundingBox().getCenterPoint();
        System.out.println("Center point, lat: " + centerPoint.getLatitude() + " , lon: " + centerPoint.getLongitude());
    }

    private int calculateCommonPrefixLength(String leftBottomGeoHash, String topRightGeoHash) {
        int prefixLength = 0;
        int minLength = Math.min(leftBottomGeoHash.length(), topRightGeoHash.length());
        for (int i = 0; i < minLength; i++) {
            if (leftBottomGeoHash.charAt(i) == topRightGeoHash.charAt(i)) {
                prefixLength++;
            } else {
                break;
            }
        }
        return prefixLength + 2;
    }
}
