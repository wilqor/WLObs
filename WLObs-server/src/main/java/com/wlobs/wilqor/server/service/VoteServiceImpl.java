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
import com.wlobs.wilqor.server.persistence.model.Vote;
import com.wlobs.wilqor.server.persistence.repository.VoteRepository;
import com.wlobs.wilqor.server.rest.model.ExistingVoteDto;
import com.wlobs.wilqor.server.rest.model.NewVoteDto;
import com.wlobs.wilqor.server.service.exceptions.ObservationNotFoundException;
import com.wlobs.wilqor.server.service.exceptions.VoteAlreadyCastedException;
import com.wlobs.wilqor.server.service.exceptions.VoteNotFoundException;
import com.wlobs.wilqor.server.service.exceptions.VoteOnSelfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wilqor
 */
@Service
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final ObservationService observationService;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, ObservationService observationService) {
        this.voteRepository = voteRepository;
        this.observationService = observationService;
    }

    @Override
    public ExistingVoteDto castAndReturnVote(String login, NewVoteDto newVoteDto) {
        Optional<Observation> voteSubjectOptional = observationService.getObservation(newVoteDto.getObservationId());
        Observation voteSubject = voteSubjectOptional.orElseThrow(() -> new ObservationNotFoundException(newVoteDto.getObservationId()));
        if (voteSubject.getAuthor().equals(login)) {
            throw new VoteOnSelfException(login, voteSubject.getId());
        }
        Optional<Vote> possibleDuplicate = voteRepository.findByVoterAndObservationId(login, newVoteDto.getObservationId());
        possibleDuplicate.ifPresent(p -> {
            throw new VoteAlreadyCastedException(login, voteSubject.getId());
        });
        Vote created = new Vote.Builder()
                .voter(login)
                .dateUtcTimestamp(newVoteDto.getDateUtcTimestamp())
                .observationId(newVoteDto.getObservationId())
                .observationOwner(voteSubject.getAuthor())
                .speciesStub(voteSubject.getSpeciesStub())
                .build();
        voteRepository.save(created);
        return convertVoteToDto(created);
    }

    @Override
    public ExistingVoteDto removeAndReturnVote(String login, String voteId) {
        Optional<Vote> foundOptional = voteRepository.findByVoterAndId(login, voteId);
        Vote found = foundOptional.orElseThrow(() -> new VoteNotFoundException(voteId));
        voteRepository.delete(found);
        return convertVoteToDto(found);
    }

    @Override
    public List<ExistingVoteDto> getUserVotes(String login) {
        return voteRepository.findByVoter(login).stream()
                .map(
                        this::convertVoteToDto
                )
                .collect(Collectors.toList());
    }

    private ExistingVoteDto convertVoteToDto(Vote v) {
        return new ExistingVoteDto.Builder()
                .id(v.getId())
                .dateUtcTimestamp(v.getDateUtcTimestamp())
                .voter(v.getVoter())
                .observationId(v.getObservationId())
                .observationOwner(v.getObservationOwner())
                .speciesStub(v.getSpeciesStub())
                .build();
    }
}
