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

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.wlobs.wilqor.server.config.GeoHashConstants;
import com.wlobs.wilqor.server.service.model.AggregationRequest;
import org.springframework.stereotype.Service;

/**
 * @author wilqor
 */
@Service
public class GeoHashServiceImpl implements GeoHashService {
    @Override
    public String getGeoHashForLocation(double latitude, double longitude) {
        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, GeoHashConstants.GEO_HASH_PRECISION);
    }

    @Override
    public int getCommonPrefixLength(AggregationRequest.Area area) {
        String leftBottom = getGeoHashForLocation(area.getBottom(), area.getLeft());
        String topRight = getGeoHashForLocation(area.getTop(), area.getRight());
        return calculateCommonPrefixLength(leftBottom, topRight);
    }

    @Override
    public WGS84Point getPointFromGeoHash(String geoHash) {
        GeoHash hash = GeoHash.fromGeohashString(geoHash);
        return hash.getBoundingBoxCenterPoint();
    }

    private int calculateCommonPrefixLength(String hashOne, String hashTwo) {
        int prefixLength = 0;
        int minLength = Math.min(hashOne.length(), hashTwo.length());
        for (int i = 0; i < minLength; i++) {
            if (hashOne.charAt(i) == hashTwo.charAt(i)) {
                prefixLength++;
            } else {
                break;
            }
        }
        return prefixLength + GeoHashConstants.CLUSTERING_POWER;
    }
}
