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

package com.wlobs.wilqor.server.service.exceptions;

import com.wlobs.wilqor.server.rest.model.NewObservationDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author wilqor
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObservationAlreadyExistsException extends RuntimeException {
    public ObservationAlreadyExistsException(NewObservationDto observationDto) {
        super(String.format("Cannot add observation: %s, it already exists", observationDto.toString()));
    }
}
