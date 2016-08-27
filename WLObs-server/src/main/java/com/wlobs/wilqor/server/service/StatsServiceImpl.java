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

package com.wlobs.wilqor.server.service;

import com.wlobs.wilqor.server.persistence.model.MonthlyStat;
import com.wlobs.wilqor.server.persistence.model.StatMetaData;
import com.wlobs.wilqor.server.persistence.model.YearlyStat;
import com.wlobs.wilqor.server.persistence.repository.MonthlyStatRepository;
import com.wlobs.wilqor.server.persistence.repository.YearlyStatRepository;
import com.wlobs.wilqor.server.rest.model.StatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author wilqor
 */
@Service
public class StatsServiceImpl implements StatsService {
    private static final int DEFAULT_DAY_OF_MONTH = 1;
    private static final int DEFAULT_MONTH_OF_YEAR = 1;
    private static final int MONTHS_IN_YEAR = 12;

    private final MonthlyStatRepository monthlyStatRepository;
    private final YearlyStatRepository yearlyStatRepository;

    @Autowired
    public StatsServiceImpl(MonthlyStatRepository monthlyStatRepository, YearlyStatRepository yearlyStatRepository) {
        this.monthlyStatRepository = monthlyStatRepository;
        this.yearlyStatRepository = yearlyStatRepository;
    }

    @Override
    public StatsDto getMonthlyStats(int year, int month, StatMetaData.StatOperation operation) {
        LocalDate date = LocalDate.of(year, month, DEFAULT_DAY_OF_MONTH);
        StatMetaData metaData = new StatMetaData(date, operation);
        Optional<MonthlyStat> monthlyStat = monthlyStatRepository.findByMetaData(metaData);
        Map<String, Integer> persistent = new TreeMap<>();
        monthlyStat.ifPresent(m -> persistent.putAll(m.getDailyCount()));
        return new StatsDto(
                mergeZeroInitializedWithPersistent(
                        buildZeroInitializedStatMap(date.getMonth().maxLength()),
                        persistent
                )
        );
    }

    @Override
    public StatsDto getYearlyStats(int year, StatMetaData.StatOperation operation) {
        LocalDate date = LocalDate.of(year, DEFAULT_MONTH_OF_YEAR, DEFAULT_DAY_OF_MONTH);
        StatMetaData metaData = new StatMetaData(date, operation);
        Optional<YearlyStat> yearlyStat = yearlyStatRepository.findByMetaData(metaData);
        Map<String, Integer> persistent = new TreeMap<>();
        yearlyStat.ifPresent(y -> persistent.putAll(y.getMonthlyCount()));
        return new StatsDto(
                mergeZeroInitializedWithPersistent(
                        buildZeroInitializedStatMap(MONTHS_IN_YEAR),
                        persistent
                )
        );
    }

    @Override
    public void incrementOperationStats(StatMetaData.StatOperation operation) {
        LocalDate now = LocalDate.now();
        monthlyStatRepository.incrementStats(
                new StatMetaData(LocalDate.of(now.getYear(), now.getMonth(), DEFAULT_DAY_OF_MONTH), operation),
                now.getDayOfMonth());
        yearlyStatRepository.incrementStats(
                new StatMetaData(LocalDate.of(now.getYear(), DEFAULT_MONTH_OF_YEAR, DEFAULT_DAY_OF_MONTH), operation),
                now.getMonthValue());
    }

    private Map<String, Integer> buildZeroInitializedStatMap(int length) {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 1; i <= length; i++) {
            map.put(String.valueOf(i), 0);
        }
        return map;
    }

    private Map<String, Integer> mergeZeroInitializedWithPersistent(Map<String, Integer> zeroInitialized, Map<String, Integer> persistent) {
        Map<String, Integer> merged = new TreeMap<>(zeroInitialized);
        persistent.entrySet()
                .stream()
                .filter(e -> merged.containsKey(e.getKey()))
                .forEach(e -> merged.put(e.getKey(), e.getValue()));
        return merged;
    }
}
