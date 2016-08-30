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
app.factory('StatsService', function ($http) {
    function convertToChartFormat(response) {
        var labelToValue = response.labelToValue;
        var labels = [];
        var data = [];
        for (var point in labelToValue) {
            labels.push(point + '');
            data.push(labelToValue[point]);
        }
        return {
            labels: labels,
            data: data
        };
    }

    var StatsService = {
        getYearlyStats: function (operationParam, year, callback) {
            $http.get('/stats/yearly', {
                params: {
                    operation: operationParam,
                    year: year
                }
            }).success(function (data) {
                callback(convertToChartFormat(data));
            })
        },
        getMonthlyStats: function (operationParam, year, month, callback) {
            $http.get('/stats/monthly', {
                params: {
                    operation: operationParam,
                    year: year,
                    month: month
                }
            }).success(function (data) {
                callback(convertToChartFormat(data));
            })
        }
    };
    return StatsService;
});