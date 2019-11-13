package com.github.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VotesCalculateTask implements ForeachAction<Long, String> {
    private static Logger logger = LoggerFactory.getLogger(VotesCalculateTask.class);
    private final Map<String, Long> votesCounterMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void apply(Long key, String value) {
        try{
            Candidate candidate = objectMapper.readValue(value, Candidate.class);
            logger.info("Received {} with votes {}",candidate.getName(), candidate.getVotes());
            if(! votesCounterMap.containsKey(candidate.getName())){
                votesCounterMap.put(candidate.getName(), new Long(0));
            }
            Long totalVotes = votesCounterMap.get(candidate.getName());
            totalVotes = totalVotes + candidate.getVotes();
            votesCounterMap.put(candidate.getName(), totalVotes);
        }catch(Exception ex){
            logger.error("Cannot parse candidate, here is the exception : ", ex.getMessage());
        }
    }
}
