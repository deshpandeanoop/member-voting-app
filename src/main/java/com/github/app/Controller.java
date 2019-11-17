package com.github.app;

import com.github.app.model.Candidate;
import com.github.app.websocket.VoteCountPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/votes")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final VotesCalculator votesCalculator;
    private final VoteCountPublisher voteCountEventPublisher;

    public Controller(VotesCalculator votesCalculator, VoteCountPublisher voteCountEventPublisher) {
        this.votesCalculator = votesCalculator;
        this.voteCountEventPublisher = voteCountEventPublisher;
    }

    @PostMapping
    public String castVote(@RequestBody Candidate candidate) {
        logger.info("Casting vote for candidate {}", candidate.getName());
        candidate.setTotalVotes(votesCalculator.castVoteAndGetTotalVotes(candidate));
        logger.info("Publishing event for candidate {}", candidate.getName());
        voteCountEventPublisher.publishEvent(candidate);
        return "Vote casted successfully";
    }
}
