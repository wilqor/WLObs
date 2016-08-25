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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author wilqor
 */
public class ObservationRepositoryImpl implements CustomObservationRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ObservationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementObservationVotesCount(String observationId) {
        doModifyObservationVotesCountByOne(observationId, true);
    }

    @Override
    public void decrementObservationVotesCount(String observationId) {
        doModifyObservationVotesCountByOne(observationId, false);
    }

    private void doModifyObservationVotesCountByOne(String observationId, boolean increment) {
        Query query = new Query(Criteria.where("id").is(observationId));
        Update update = new Update().inc("votesCount", increment ? 1 : -1);
        mongoTemplate.findAndModify(query, update, Observation.class);
    }
}
