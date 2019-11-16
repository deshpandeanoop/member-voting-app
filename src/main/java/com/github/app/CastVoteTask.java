package com.github.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CastVoteTask implements ForeachAction<Long, String> {
    private static Logger logger = LoggerFactory.getLogger(CastVoteTask.class);
    private final ObjectMapper objectMapper;
    private final VotesCalculator votesCalculator;

    public CastVoteTask(VotesCalculator votesCalculator, ObjectMapper objectMapper) {
        this.votesCalculator = votesCalculator;
        this.objectMapper = objectMapper;
    }

    @Override
    public void apply(Long key, String value) {
        try{
            Candidate candidate = objectMapper.readValue(value, Candidate.class);
            logger.info("Received {} with votes {}",candidate.getName(), candidate.getVotes());
            votesCalculator.castVote(candidate);
        }catch(Exception ex){
            logger.error("Cannot parse candidate, here is the exception : ", ex.getMessage());
        }
    }
}
