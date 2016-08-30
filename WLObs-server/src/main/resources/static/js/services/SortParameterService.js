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
app.factory('SortParameterService', function () {
    function convertSortParameterToDirection(sortParameter) {
        var direction = 'ASC';
        if (sortParameter.charAt(0) == '-') {
            direction = 'DESC';
        }
        return direction;
    }

    function stripSortParameterOfDirection(sortParameter) {
        var sort = sortParameter;
        if (sortParameter.charAt(0) == '-') {
            sort = sortParameter.substr(1);
        }
        return sort;
    }

    function convertPageNumberToOneStartingWithZero(pageParameter) {
        console.log('received page parameter: ' + pageParameter);
        return pageParameter - 1;
    }

    var SortParameterService = {
        convertToPaginationParameters: function (sortParameter, pageNumber) {
            return {
                pageNo: convertPageNumberToOneStartingWithZero(pageNumber),
                sortBy: stripSortParameterOfDirection(sortParameter),
                direction: convertSortParameterToDirection(sortParameter)
            }
        }
    };
    return SortParameterService;
});