package com.github.app.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.app.VotesCalculator;
import com.github.app.model.Candidate;
import com.github.app.websocket.VoteCountPublisher;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VotesProcessor implements ForeachAction<Long, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VotesProcessor.class);
    private final ObjectMapper objectMapper;
    private final VotesCalculator votesCalculator;
    private final VoteCountPublisher voteCountPublisher;

    public VotesProcessor(VotesCalculator votesCalculator, ObjectMapper objectMapper, VoteCountPublisher voteCountPublisher) {
        this.votesCalculator = votesCalculator;
        this.objectMapper = objectMapper;
        this.voteCountPublisher = voteCountPublisher;
    }

    @Override
    public void apply(Long key, String value) {
        try{
            Candidate candidate = objectMapper.readValue(value, Candidate.class);
            LOGGER.info("Received {} with votes {}",candidate.getName(), candidate.getVotes());
            candidate.setTotalVotes(votesCalculator.castVoteAndGetTotalVotes(candidate));
            LOGGER.info("Publishing vote count envent for candidate {}", candidate.getName());
            voteCountPublisher.publishEvent(candidate);
        }catch(Exception ex){
            LOGGER.error("Cannot parse candidate, here is the exception : ", ex.getMessage());
        }
    }
}
