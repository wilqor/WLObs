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

package com.wlobs.wilqor.mobile.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author wilqor
 */
public final class AggregationRequestDto {
    @Expose
    @SerializedName("area")
    private Area area;

    @Expose
    @SerializedName("timeRange")
    private TimeRange timeRange;

    @Expose
    @SerializedName("speciesClass")
    private SpeciesStub.Class speciesClass;

    @Expose
    @SerializedName("speciesLatinName")
    private String speciesLatinName;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public SpeciesStub.Class getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(SpeciesStub.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public String getSpeciesLatinName() {
        return speciesLatinName;
    }

    public void setSpeciesLatinName(String speciesLatinName) {
        this.speciesLatinName = speciesLatinName;
    }

    @Override
    public String toString() {
        return "AggregationRequestDto{" +
                "area=" + area +
                ", timeRange=" + timeRange +
                ", speciesClass=" + speciesClass +
                ", speciesLatinName='" + speciesLatinName + '\'' +
                '}';
    }

    public static final class Area {
        @Expose
        @SerializedName("bottom")
        private double bottom;

        @Expose
        @SerializedName("top")
        private double top;

        @Expose
        @SerializedName("left")
        private double left;

        @Expose
        @SerializedName("right")
        private double right;

        public double getBottom() {
            return bottom;
        }

        public void setBottom(double bottom) {
            this.bottom = bottom;
        }

        public double getTop() {
            return top;
        }

        public void setTop(double top) {
            this.top = top;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getRight() {
            return right;
        }

        public void setRight(double right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return "Area{" +
                    "bottom=" + bottom +
                    ", top=" + top +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    public static final class TimeRange implements Serializable {
        public TimeRange() {
        }

        public TimeRange(long dateFromUtcTimestamp, long dateToUtcTimestamp) {
            this.dateFromUtcTimestamp = dateFromUtcTimestamp;
            this.dateToUtcTimestamp = dateToUtcTimestamp;
        }

        @Expose
        @SerializedName("dateFromUtcTimestamp")
        private long dateFromUtcTimestamp;

        @Expose
        @SerializedName("dateFromUtcTimestamp")
        private long dateToUtcTimestamp;

        public long getDateFromUtcTimestamp() {
            return dateFromUtcTimestamp;
        }

        public void setDateFromUtcTimestamp(long dateFromUtcTimestamp) {
            this.dateFromUtcTimestamp = dateFromUtcTimestamp;
        }

        public long getDateToUtcTimestamp() {
            return dateToUtcTimestamp;
        }

        public void setDateToUtcTimestamp(long dateToUtcTimestamp) {
            this.dateToUtcTimestamp = dateToUtcTimestamp;
        }

        @Override
        public String toString() {
            return "TimeRange{" +
                    "dateFromUtcTimestamp=" + dateFromUtcTimestamp +
                    ", dateToUtcTimestamp=" + dateToUtcTimestamp +
                    '}';
        }
    }
}
