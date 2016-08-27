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

import com.wlobs.wilqor.server.auth.annotations.RequiredAdminRole;
import com.wlobs.wilqor.server.persistence.model.StatMetaData;
import com.wlobs.wilqor.server.rest.model.StatsDto;
import com.wlobs.wilqor.server.rest.validation.MonthValidator;
import com.wlobs.wilqor.server.rest.validation.YearValidator;
import com.wlobs.wilqor.server.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author wilqor
 */
@RestController
@RequiredAdminRole
@RequestMapping("/stats")
public class StatsController {
    private final MonthValidator monthValidator;
    private final YearValidator yearValidator;
    private final StatsService statsService;

    @Autowired
    public StatsController(MonthValidator monthValidator, YearValidator yearValidator, StatsService statsService) {
        this.monthValidator = monthValidator;
        this.yearValidator = yearValidator;
        this.statsService = statsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/monthly")
    public StatsDto getMonthlyStats(@RequestParam(value = "year") Optional<Integer> year,
                                    @RequestParam(value = "month") Optional<Integer> month,
                                    @RequestParam(value = "operation") Optional<StatMetaData.StatOperation> operation) {
        LocalDate now = LocalDate.now();
        int requestYear = validateAndGetYear(year, now);
        int requestMonth = validateAndGetMonth(month, now);
        StatMetaData.StatOperation requestOperation = getOperationOrDefault(operation);
        return statsService.getMonthlyStats(requestYear, requestMonth, requestOperation);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/yearly")
    public StatsDto getYearlyStats(@RequestParam(value = "year") Optional<Integer> year,
                                   @RequestParam(value = "operation") Optional<StatMetaData.StatOperation> operation) {
        LocalDate now = LocalDate.now();
        int requestYear = validateAndGetYear(year, now);
        StatMetaData.StatOperation requestOperation = getOperationOrDefault(operation);
        return statsService.getYearlyStats(requestYear, requestOperation);
    }

    private int validateAndGetYear(Optional<Integer> yearParameter, LocalDate now) {
        yearParameter.ifPresent(yearValidator::validateYear);
        return yearParameter.orElse(now.getYear());
    }

    private int validateAndGetMonth(Optional<Integer> monthParameter, LocalDate now) {
        monthParameter.ifPresent(monthValidator::validateMonth);
        return monthParameter.orElse(now.getMonthValue());
    }

    private StatMetaData.StatOperation getOperationOrDefault(Optional<StatMetaData.StatOperation> operation) {
        return operation.orElse(StatMetaData.StatOperation.USER_REGISTRATION);
    }
}
