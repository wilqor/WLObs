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

import com.wlobs.wilqor.server.persistence.model.StatMetaData;
import com.wlobs.wilqor.server.persistence.model.YearlyStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wilqor
 */
public class YearlyStatRepositoryImpl implements CustomYearlyStatRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public YearlyStatRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementStats(StatMetaData metaData, int monthOfYear) {
        String id = Stream.of(
                String.valueOf(metaData.getDate().getYear()),
                metaData.getStatOperation().name())
                .collect(Collectors.joining(StatMetaData.ID_PARTS_JOINER));
        Query query = new Query(Criteria.where("id").is(id)
                .and("metaData").is(metaData));
        Update update = new Update().inc("monthlyCount." + monthOfYear, 1);
        mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().upsert(true), YearlyStat.class);
    }
}
