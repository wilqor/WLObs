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

/**
 * @author wilqor
 */
public class AggregatedObservation {
    private String _id;
    private int count;

    public AggregatedObservation() {
    }

    public AggregatedObservation(String _id, int count) {
        this._id = _id;
        this.count = count;
    }

    public String get_id() {
        return _id;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "AggregatedObservation{" +
                "_id='" + _id + '\'' +
                ", count=" + count +
                '}';
    }
}
