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

package com.wlobs.wilqor.mobile.rest.task;

import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import retrofit2.Response;

/**
 * @author wilqor
 */
final class DefaultErrorResponseHandler implements ErrorResponseHandler {
    private final ErrorResponseHandler decorated;

    DefaultErrorResponseHandler(ErrorResponseHandler decorated) {
        this.decorated = decorated;
    }

    @Override
    public void handleErrorResponse(Response<?> errorResponse) throws UnauthorizedException, ErrorResponseException {
        decorated.handleErrorResponse(errorResponse);
        throw new ErrorResponseException();
    }
}
