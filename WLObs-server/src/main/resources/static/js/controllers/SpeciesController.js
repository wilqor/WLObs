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
app.controller('SpeciesController', function (SpeciesService, $scope) {
    $scope.page = 1;
    $scope.order = "LATIN_NAME";
    $scope.votes = [];
    $scope.total = 1;

    $scope.getSpeciesPage = function () {
        $scope.promise = SpeciesService.getSpeciesPage($scope.order, $scope.page)
            .then(function (data) {
                $scope.species = data.records;
                $scope.total = data.totalElements;
            });
    };

    $scope.getSpeciesPage();
});