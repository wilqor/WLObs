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
var app = angular.module('AdminPanelApp',
    ['ngRoute', 'ngStorage', 'ngMaterial', 'ngMessages', 'angular-jwt', 'chart.js', 'md.data.table']);

app.config(function ($routeProvider) {
    $routeProvider
        .when('/users', {
            templateUrl: 'views/users.html',
            title: 'WLObs Admin Panel - Users',
            controller: 'UsersController'
        })
        .when('/species', {
            templateUrl: 'views/species.html',
            title: 'WLObs Admin Panel - Species',
            controller: 'SpeciesController'
        })
        .when('/observations', {
            templateUrl: 'views/observations.html',
            title: 'WLObs Admin Panel - Observations',
            controller: 'ObservationsController'
        })
        .when('/votes', {
            templateUrl: 'views/votes.html',
            title: 'WLObs Admin Panel - Votes',
            controller: 'VotesController'
        })
        .when('/stats', {
            templateUrl: 'views/stats.html',
            title: 'WLObs Admin Panel - Statistics',
            controller: 'StatsController',
            controllerAs: 'statsCtrl'
        })
        .when('/login', {
            templateUrl: 'views/login.html',
            title: 'WLObs Admin Panel - Log in',
            controller: 'LoginController',
            controllerAs: 'loginCtrl'
        })
        .otherwise({
            redirectTo: '/users'
        });
});

app.run(function ($rootScope, $route, $location, AuthService) {
    $rootScope.$on('$routeChangeSuccess', function () {
        document.title = $route.current.title;
    });
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        var publicPages = ['/login'];
        var restrictedPage = publicPages.indexOf($location.path()) === -1;
        if (restrictedPage && !AuthService.isLoggedIn()) {
            $location.path('/login');
        }
    });
});

app.factory('authTokenInjector', ['TokenService', function (TokenService) {
    var authTokenInjector = {
        request: function (config) {
            var authToken = TokenService.getAuthToken();
            if (authToken != undefined) {
                config.headers['Authorization'] = 'Bearer ' + authToken;
            }
            return config;
        }
    };
    return authTokenInjector;
}]);

app.factory('unauthorizedHandler', ['$q', '$injector', function ($q, $injector) {
    var unauthorizedHandler = {
        responseError: function (rejection) {
            var authService = $injector.get('AuthService');
            if (authService.isLoggedIn() && rejection.status == 401) {
                authService.logOut();
            }
            return $q.reject(rejection);
        }
    };
    return unauthorizedHandler;
}]);

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('authTokenInjector');
    $httpProvider.interceptors.push('unauthorizedHandler');
});