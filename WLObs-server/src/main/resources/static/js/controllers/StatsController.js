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

/**
 * @author wilqor
 */
app.controller('StatsController', function (StatsService) {
    this.showResults = function () {
        this.showChart = false;
        if (this.monthly) {
            StatsService.getMonthlyStats(this.operation.param, this.year, this.month, this.statsCallback);
        } else {
            StatsService.getYearlyStats(this.operation.param, this.year, this.statsCallback);
        }
    };
    this.init = function () {
        // operation selection
        this.operations = [
            {
                name: 'User registration',
                param: 'USER_REGISTRATION'
            },
            {
                name: 'User authorization',
                param: 'USER_AUTHORIZATION'
            },
            {
                name: 'User password change',
                param: 'USER_PASSWORD_CHANGE'
            },
            {
                name: 'Observation creation',
                param: 'OBSERVATION_CREATION'
            },
            {
                name: 'Observation removal',
                param: 'OBSERVATION_REMOVAL'
            },
            {
                name: 'Observation restriction change',
                param: 'OBSERVATION_RESTRICTION_CHANGE'
            },
            {
                name: 'Vote adding',
                param: 'VOTE_ADDING'
            },
            {
                name: 'Vote removal',
                param: 'VOTE_REMOVAL'
            },
            {
                name: 'Aggregation requests',
                param: 'AGGREGATION_REQUEST'
            }
        ];
        this.operation = this.operations[0];
        // year and month selection
        var now = new Date();
        var currentYear = now.getFullYear();
        this.year = currentYear;
        this.month = now.getMonth() + 1;
        this.years = range(1970, currentYear).reverse();
        this.months = range(1, 12);
        this.monthly = true;
        // helper function for generating allowed year/month ranges
        function range(lowEnd, highEnd) {
            var result = [];
            for (var i = lowEnd; i <= highEnd; i++) {
                result.push(i);
            }
            return result;
        }

        this.showChart = false;
        // callback after receiving stats from service
        var statsCtrl = this;
        this.statsCallback = function (response) {
            statsCtrl.labels = response.labels;
            statsCtrl.data = [response.data];
            statsCtrl.showChart = true;
        }
    };
    this.init();
});
