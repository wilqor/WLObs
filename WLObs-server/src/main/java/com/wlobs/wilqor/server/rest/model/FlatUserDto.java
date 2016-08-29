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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wilqor
 */
public class FlatUserDto {
    public static final String ROLES_JOINER = ",";

    @JsonProperty
    private final String id;

    @JsonProperty
    private final String login;

    @JsonProperty
    private final String rolesCsv;

    @JsonProperty
    private final int observationsCount;

    @JsonProperty
    private final int votesCasted;

    @JsonProperty
    private final int votesReceived;

    private FlatUserDto(String id, String login, String rolesCsv, int observationsCount, int votesCasted, int votesReceived) {
        this.id = id;
        this.login = login;
        this.rolesCsv = rolesCsv;
        this.observationsCount = observationsCount;
        this.votesCasted = votesCasted;
        this.votesReceived = votesReceived;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getRolesCsv() {
        return rolesCsv;
    }

    public int getObservationsCount() {
        return observationsCount;
    }

    public int getVotesCasted() {
        return votesCasted;
    }

    public int getVotesReceived() {
        return votesReceived;
    }

    @Override
    public String toString() {
        return "FlatUserDto{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", rolesCsv='" + rolesCsv + '\'' +
                ", observationsCount=" + observationsCount +
                ", votesCasted=" + votesCasted +
                ", votesReceived=" + votesReceived +
                '}';
    }

    public static class Builder {
        private String id;
        private String login;
        private String rolesCsv;
        private int observationsCount;
        private int votesCasted;
        private int votesReceived;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder rolesCsv(String rolesCsv) {
            this.rolesCsv = rolesCsv;
            return this;
        }

        public Builder observationsCount(int observationsCount) {
            this.observationsCount = observationsCount;
            return this;
        }

        public Builder votesCasted(int votesCasted) {
            this.votesCasted = votesCasted;
            return this;
        }

        public Builder votesReceived(int votesReceived) {
            this.votesReceived = votesReceived;
            return this;
        }

        public FlatUserDto build() {
            return new FlatUserDto(
                    id,
                    login,
                    rolesCsv,
                    observationsCount,
                    votesCasted,
                    votesReceived
            );
        }
    }
}
