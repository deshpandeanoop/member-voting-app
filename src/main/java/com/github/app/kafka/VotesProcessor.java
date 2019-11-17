package com.github.app.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.app.VotesCalculator;
import com.github.app.model.Candidate;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VotesProcessor implements ForeachAction<Long, String> {
    private static Logger logger = LoggerFactory.getLogger(VotesProcessor.class);
    private final ObjectMapper objectMapper;
    private final VotesCalculator votesCalculator;

    public VotesProcessor(VotesCalculator votesCalculator, ObjectMapper objectMapper) {
        this.votesCalculator = votesCalculator;
        this.objectMapper = objectMapper;
    }

    @Override
    public void apply(Long key, String value) {
        try{
            Candidate candidate = objectMapper.readValue(value, Candidate.class);
            logger.info("Received {} with votes {}",candidate.getName(), candidate.getVotes());
            votesCalculator.castVoteAndGetTotalVotes(candidate);
        }catch(Exception ex){
            logger.error("Cannot parse candidate, here is the exception : ", ex.getMessage());
        }
    }
}
