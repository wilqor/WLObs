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
app.controller('IndexController', function ($mdSidenav, $location, AuthService) {
    this.toggleSidenav = function () {
        $mdSidenav('left').toggle();
    };
    this.goTo = function (destination) {
        $location.path(destination);
        $mdSidenav('left').close();
    };
    this.isActive = function (viewLocation) {
        return (viewLocation === $location.path());
    };
    this.logOut = function () {
        AuthService.logOut();
    };
    this.shouldDisplayNav = function () {
        return AuthService.isLoggedIn();
    }
});