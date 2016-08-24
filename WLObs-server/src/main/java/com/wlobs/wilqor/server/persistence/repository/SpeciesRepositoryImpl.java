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

import com.wlobs.wilqor.server.persistence.model.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Locale;
import java.util.Map;

/**
 * @author wilqor
 */
public class SpeciesRepositoryImpl implements CustomSpeciesRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SpeciesRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void upsertSpecies(Species.Class speciesClass, String latinName, Map<Locale, String> localizedNames) {
        Query query = new Query(Criteria.where("speciesClass").is(speciesClass)
                .and("latinName").is(latinName));
        Update update = new Update().set("localizedNames", localizedNames);
        mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().upsert(true), Species.class);
    }
}
