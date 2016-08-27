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

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

/**
 * @author wilqor
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.wlobs.wilqor.server.persistence")
public class MongoConfig extends AbstractMongoConfiguration {
    private final String database;
    private final String host;
    private final int port;
    private final String userName;
    private final String password;

    @Autowired
    public MongoConfig(@Value("${mongo.database}") String database,
                       @Value("${mongo.host}") String host,
                       @Value("${mongo.port}") int port,
                       @Value("${mongo.userName}") String userName,
                       @Value("${mongo.password}") String password) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(
                Collections.singletonList(
                        new ServerAddress(host, port)),
                Collections.singletonList(
                        MongoCredential.createCredential(userName, database, password.toCharArray())));
    }
}
