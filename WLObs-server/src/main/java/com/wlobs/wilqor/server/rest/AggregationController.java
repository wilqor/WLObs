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

package com.wlobs.wilqor.server.rest;

import com.wlobs.wilqor.server.auth.annotations.RequiredUserOrAdminRole;
import com.wlobs.wilqor.server.rest.model.AggregationRequestDto;
import com.wlobs.wilqor.server.rest.model.AggregationResponseDto;
import com.wlobs.wilqor.server.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author wilqor
 */
@RestController
@RequestMapping("/aggregations")
@RequiredUserOrAdminRole
public class AggregationController {
    private final ObservationService observationService;

    @Autowired
    public AggregationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public AggregationResponseDto getAggregatedObservations(@RequestBody @Valid AggregationRequestDto aggregationRequestDto) {
        return observationService.getAggregatedObservations(aggregationRequestDto);
    }
}
