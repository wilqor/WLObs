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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wilqor
 */
@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String login;

    private String passwordHash;

    @CreatedDate
    private Date createdDate;

    private List<String> refreshTokens;

    private List<Role> roles;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }

    private UserStats userStats;

    public static class UserStats {
        private int observationsCount;
        private int votesCasted;
        private int votesReceived;

        public int getObservationsCount() {
            return observationsCount;
        }

        public void setObservationsCount(int observationsCount) {
            this.observationsCount = observationsCount;
        }

        public int getVotesCasted() {
            return votesCasted;
        }

        public void setVotesCasted(int votesCasted) {
            this.votesCasted = votesCasted;
        }

        public int getVotesReceived() {
            return votesReceived;
        }

        public void setVotesReceived(int votesReceived) {
            this.votesReceived = votesReceived;
        }

        @Override
        public String toString() {
            return "UserStats{" +
                    "observationsCount=" + observationsCount +
                    ", votesCasted=" + votesCasted +
                    ", votesReceived=" + votesReceived +
                    '}';
        }
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<String> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserStats getUserStats() {
        return userStats;
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", createdDate=" + createdDate +
                ", refreshTokens=" + refreshTokens +
                ", roles=" + roles +
                ", userStats=" + userStats +
                '}';
    }

    public static class Builder {
        private String login;
        private String passwordHash;
        private List<String> refreshTokens;
        private List<Role> roles;
        private UserStats userStats;

        public Builder() {
            login = "";
            passwordHash = "";
            refreshTokens = new ArrayList<>();
            roles = new ArrayList<>();
            userStats = new UserStats();
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder refreshTokens(List<String> refreshTokens) {
            this.refreshTokens = refreshTokens;
            return this;
        }

        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder userStats(UserStats userStats) {
            this.userStats = userStats;
            return this;
        }

        public User build() {
            User created = new User();
            created.setLogin(login);
            created.setPasswordHash(passwordHash);
            created.setRefreshTokens(refreshTokens);
            created.setRoles(roles);
            created.setUserStats(userStats);
            return created;
        }
    }
}
