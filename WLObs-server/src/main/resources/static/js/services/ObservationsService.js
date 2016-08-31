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
app.factory('ObservationsService', function ($http, SortParameterService) {
    var ObservationsService = {
        getObservationsPage: function (sortParameter, pageNumber) {
            return $http.get('/observations', {
                params: SortParameterService.convertToPaginationParameters(sortParameter, pageNumber)
            }).then(function (response) {
                return response.data;
            })
        }
    };
    return ObservationsService;
});