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

package com.wlobs.wilqor.server.persistence.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wlobs.wilqor.server.persistence.model.AggregatedObservation;
import com.wlobs.wilqor.server.persistence.model.Observation;
import com.wlobs.wilqor.server.service.model.AggregationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author wilqor
 */
public class ObservationRepositoryImpl implements CustomObservationRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ObservationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementObservationVotesCount(String observationId) {
        doModifyObservationVotesCountByOne(observationId, true);
    }

    @Override
    public void decrementObservationVotesCount(String observationId) {
        doModifyObservationVotesCountByOne(observationId, false);
    }

    private void doModifyObservationVotesCountByOne(String observationId, boolean increment) {
        Query query = new Query(Criteria.where("id").is(observationId));
        Update update = new Update().inc("votesCount", increment ? 1 : -1);
        mongoTemplate.findAndModify(query, update, Observation.class);
    }

    @Override
    public List<AggregatedObservation> getAggregatedObservations(AggregationRequest request, int commonPrefixLength) {
        Aggregation aggregation = newAggregation(
                match(buildCriteriaFromRequest(request)),
                project("location").andExclude("_id").andExpression(String.format("substr('$geoHash', 0, %d)", commonPrefixLength)).as("geo"),
                group("$geo").count().as("count"),
                project("count").and("_id")
        );
        return mongoTemplate.aggregate(aggregation, "observation", AggregatedObservation.class).getMappedResults();
    }

    @Override
    public List<Observation> getRawObservations(AggregationRequest request) {
        return mongoTemplate.find(new Query(buildCriteriaFromRequest(request)), Observation.class);
    }

    private Criteria buildCriteriaFromRequest(AggregationRequest request) {
        Criteria criteria = Criteria.where("restricted").is(false);
        List<Criteria> nextCriteria = new ArrayList<>();
        request.getSpeciesClassOptional().ifPresent(sc -> nextCriteria.add(
                Criteria.where("speciesStub.speciesClass").is(sc.name())));
        request.getSpeciesLatinNameOptional().ifPresent(sln -> nextCriteria.add(
                Criteria.where("speciesStub.latinName").is(sln)));
        request.getTimeRangeOptional().ifPresent(tr -> nextCriteria.add(
                Criteria.where("dateTimestamp").gte(tr.getDateFromUtcTimestamp())
                        .lte(tr.getDateToUtcTimestamp())));
        // add to list to ensure that the list of criteria added with 'AND' is not empty
        nextCriteria.add(Criteria.where("location").is(getGeoWithinObject(request.getArea())));
        criteria = criteria.andOperator(nextCriteria.toArray(new Criteria[nextCriteria.size()]));
        return criteria;
    }

    // has to be done manually, as the default solution: using "Criteria.where("location").within(new Box(..."
    // leads to #org.bson.codecs.configuration.CodecConfigurationException
    private DBObject getGeoWithinObject(AggregationRequest.Area area) {
        List<Double[]> boundingBox;
        boundingBox = new ArrayList<>(2);
        boundingBox.add(new Double[]{area.getLeft(), area.getBottom()});
        boundingBox.add(new Double[]{area.getRight(), area.getTop()});
        return new BasicDBObject("$geoWithin",
                new BasicDBObject("$box", boundingBox)
        );
    }
}
