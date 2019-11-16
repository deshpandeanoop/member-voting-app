package com.github.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final List<SseEmitter> subscribers;
    private final VotesCalculator votesCalculator;
    private final VoteCountEventPublisher voteCountEventPublisher;

    public Controller(List<SseEmitter> subscribers, VotesCalculator votesCalculator
            , VoteCountEventPublisher voteCountEventPublisher) {
        this.subscribers = subscribers;
        this.votesCalculator = votesCalculator;
        this.voteCountEventPublisher = voteCountEventPublisher;
    }

    @GetMapping
    public SseEmitter subscribe(){
        logger.info("Adding a new subscriber");
        SseEmitter subscriber = new SseEmitter();
        subscribers.add(subscriber);
        return subscriber;
    }

    @PostMapping
    public String castVote(@RequestBody Candidate candidate){
        logger.info("Casting vote for candidate {}", candidate.getName());
        candidate.setTotalVotes(votesCalculator.castVote(candidate));
        logger.info("Publishing event for candidate {}", candidate.getName());
        voteCountEventPublisher.publishEvent(candidate);
        return "Vote casted successfully";
    }
}
