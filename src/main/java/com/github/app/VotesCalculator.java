package com.github.app;

import com.github.app.model.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VotesCalculator {
    private static final Logger logger = LoggerFactory.getLogger(VotesCalculator.class);
    private final Map<String, Long> votesCounterMap = new HashMap<>();

    public Long castVote(Candidate candidate){
        logger.info("Adding votes {} for candidate {}", candidate.getVotes(), candidate.getName());
        if(! votesCounterMap.containsKey(candidate.getName())){
            votesCounterMap.put(candidate.getName(), 0L);
        }
        Long totalVotes = votesCounterMap.get(candidate.getName());
        totalVotes = totalVotes + candidate.getVotes();
        votesCounterMap.put(candidate.getName(), totalVotes);
        return totalVotes;
    }
}
