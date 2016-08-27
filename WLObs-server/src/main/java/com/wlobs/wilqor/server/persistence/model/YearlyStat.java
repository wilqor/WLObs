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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @author wilqor
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "op_and_date_idx",
                def = "{'metaData.statOperation': 1, " +
                        "'metaData.date': 1}"
        )
})

public class YearlyStat {
    @Id
    private String id;

    private StatMetaData metaData;

    private Map<String, Integer> monthlyCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(StatMetaData metaData) {
        this.metaData = metaData;
    }

    public Map<String, Integer> getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(Map<String, Integer> monthlyCount) {
        this.monthlyCount = monthlyCount;
    }

    @Override
    public String toString() {
        return "YearlyStat{" +
                "id='" + id + '\'' +
                ", metaData=" + metaData +
                ", monthlyCount=" + monthlyCount +
                '}';
    }
}
