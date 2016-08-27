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

package com.wlobs.wilqor.server.persistence.model;

import java.time.LocalDate;

/**
 * @author wilqor
 */
public class StatMetaData {
    public static final String ID_PARTS_JOINER = "/";

    private LocalDate date;
    private StatOperation statOperation;

    public StatMetaData() {
    }

    public StatMetaData(LocalDate date, StatOperation statOperation) {
        this.date = date;
        this.statOperation = statOperation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StatOperation getStatOperation() {
        return statOperation;
    }

    public void setStatOperation(StatOperation statOperation) {
        this.statOperation = statOperation;
    }

    @Override
    public String toString() {
        return "StatMetaData{" +
                "date=" + date +
                ", statOperation=" + statOperation +
                '}';
    }

    public enum StatOperation {
        USER_REGISTRATION,
        USER_AUTHORIZATION,
        USER_PASSWORD_CHANGE,
        OBSERVATION_CREATION,
        OBSERVATION_REMOVAL,
        OBSERVATION_RESTRICTION_CHANGE,
        VOTE_ADDING,
        VOTE_REMOVAL,
        AGGREGATION_REQUEST
    }
}
