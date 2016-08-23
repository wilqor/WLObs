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

package com.wlobs.wilqor.server.rest.advice;

import com.wlobs.wilqor.server.service.exceptions.InvalidPasswordException;
import com.wlobs.wilqor.server.service.exceptions.InvalidRefreshTokenException;
import com.wlobs.wilqor.server.service.exceptions.LoginAlreadyTakenException;
import com.wlobs.wilqor.server.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author wilqor
 */
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginAlreadyTakenException.class)
    @ResponseBody
    public String handleLoginAlreadyTaken(LoginAlreadyTakenException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public String handleUserNotFound(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseBody
    public String handleInvalidPassword(InvalidPasswordException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseBody
    public String handleInvalidRefreshToken(InvalidRefreshTokenException exception) {
        return exception.getMessage();
    }
}
