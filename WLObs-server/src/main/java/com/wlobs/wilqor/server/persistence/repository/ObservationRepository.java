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

package com.wlobs.wilqor.server.persistence.repository;

import com.wlobs.wilqor.server.persistence.model.Observation;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author wilqor
 */
public interface ObservationRepository extends PagingAndSortingRepository<Observation, String>, CustomObservationRepository {
    List<Observation> findByAuthor(String author);

    Optional<Observation> findByAuthorAndId(String author, String id);

    Optional<Observation> findByAuthorAndDateTimestampAndGeoHashAndSpeciesStub(String author, long dateTimestamp, String geoHash, Observation.SpeciesStub speciesStub);

    Optional<Observation> findById(String id);
}
