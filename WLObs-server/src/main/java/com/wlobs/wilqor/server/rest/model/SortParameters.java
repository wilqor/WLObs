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

import com.wlobs.wilqor.server.config.LocaleConstants;

/**
 * @author wilqor
 */
public class SortParameters {
    private SortParameters() {
    }

    interface TranslatesToParameter {
        String asParameter();
    }

    public enum UserSort implements TranslatesToParameter {
        ID("id"),
        LOGIN("login"),
        ROLES("roles"),
        OBSERVATIONS_COUNT("userStats.observationsCount"),
        VOTES_CASTED("userStats.votesCasted"),
        VOTES_RECEIVED("userStats.votesReceived");

        private final String parameter;

        UserSort(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String asParameter() {
            return parameter;
        }
    }

    public enum SpeciesSort implements TranslatesToParameter {
        ID("id"),
        LATIN_NAME("latinName"),
        SPECIES_CLASS("speciesClass"),
        NAME("localizedNames." + LocaleConstants.DEFAULT_LOCALE.toString()),
        OBSERVATIONS_COUNT("observationsCount");

        private final String parameter;

        SpeciesSort(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String asParameter() {
            return parameter;
        }
    }

    public enum ObservationSort implements TranslatesToParameter {
        ID("id"),
        DATE("dateTimestamp"),
        RESTRICTED("restricted"),
        AUTHOR("author"),
        VOTES("votesCount"),
        SPECIES_CLASS("speciesStub.speciesClass"),
        SPECIES_LATIN_NAME("speciesStub.latinName");

        private final String parameter;

        ObservationSort(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String asParameter() {
            return parameter;
        }
    }

    public enum VoteSort implements TranslatesToParameter {
        ID("id"),
        OBSERVATION_ID("observationId"),
        DATE("dateUtcTimestamp"),
        VOTER("voter"),
        OBSERVATION_OWNER("observationOwner"),
        SPECIES_CLASS("speciesStub.speciesClass"),
        SPECIES_LATIN_NAME("speciesStub.latinName");

        private final String parameter;

        VoteSort(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String asParameter() {
            return parameter;
        }
    }
}
