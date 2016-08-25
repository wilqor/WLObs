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

import com.wlobs.wilqor.server.persistence.model.User;
import com.wlobs.wilqor.server.persistence.model.UserStatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author wilqor
 */
public class UserRepositoryImpl implements CustomUserRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementUserStat(String login, UserStatType statType) {
        updateUserStats(login, true, statType);
    }

    @Override
    public void decrementUserStat(String login, UserStatType statType) {
        updateUserStats(login, false, statType);
    }

    private void updateUserStats(String login, boolean increment, UserStatType statType) {
        Query query = new Query(Criteria.where("login").is(login));
        Update update = new Update().inc("userStats." + statType.getMongoTemplateKey(), increment ? 1 : -1);
        mongoTemplate.findAndModify(query, update, User.class);
    }
}
