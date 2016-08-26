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

package com.wlobs.wilqor.server.rest.model;

import java.util.List;

/**
 * @author wilqor
 */
public class RecordsPageDto<T> {
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int RECORDS_PAGE_SIZE = 20;

    private final List<T> records;
    private final long totalElements;
    private final int totalPages;

    public RecordsPageDto(List<T> records, long totalElements, int totalPages) {
        this.records = records;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getRecords() {
        return records;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "RecordsPageDto{" +
                "records=" + records +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
