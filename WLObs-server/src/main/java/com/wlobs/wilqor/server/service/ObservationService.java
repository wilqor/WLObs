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

import com.wlobs.wilqor.server.persistence.model.Observation;
import com.wlobs.wilqor.server.persistence.repository.ObservationStatsModifier;
import com.wlobs.wilqor.server.rest.model.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @author wilqor
 */
public interface ObservationService extends ObservationStatsModifier {
    void addObservation(String login, NewObservationDto observationDto);

    void updateObservationRestriction(String login, String id, ObservationRestrictionDto observationRestrictionDto);

    void removeObservation(String login, String id);

    List<ExistingObservationDto> getUserObservations(String login);

    ExistingObservationDto getUserObservation(String principalLogin, String login, String observationId);

    Optional<Observation> getObservation(String observationId);

    AggregationResponseDto getAggregatedObservations(AggregationRequestDto aggregationRequestDto);

    RecordsPageDto<FlatObservationDto> getObservationsPage(int pageNumber, String sort, Sort.Direction direction);
}
