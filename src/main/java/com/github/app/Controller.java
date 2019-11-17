package com.github.app;

import com.github.app.model.Candidate;
import com.github.app.model.KafkaConfig;
import com.github.app.model.VoteCountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/votes")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final KafkaTemplate<String, Candidate> kafkaTemplate;
    private final KafkaConfig config;
    private final VotesCalculator votesCalculator;

    public Controller(KafkaTemplate<String, Candidate> kafkaTemplate, KafkaConfig config, VotesCalculator votesCalculator) {
        this.kafkaTemplate = kafkaTemplate;
        this.config = config;
        this.votesCalculator = votesCalculator;
    }

    @GetMapping
    public VoteCountResponse getTotalVotes(){
        logger.info("Getting total vote counts of all candidates");
        VoteCountResponse voteCountResponse = new VoteCountResponse();
        voteCountResponse.setCandidates(votesCalculator.getTotalVotes());
        return voteCountResponse;
    }

    @PostMapping
    public String castVote(@RequestBody Candidate candidate) {
        logger.info("Casting vote for candidate {}", candidate.getName());
        kafkaTemplate.send(config.getTopic(), candidate);
        return "Vote casted successfully";
    }
}
