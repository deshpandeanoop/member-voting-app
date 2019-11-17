package com.github.app;

import com.github.app.model.Candidate;
import com.github.app.model.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/votes")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final KafkaTemplate<String, Candidate> kafkaTemplate;
    private final KafkaConfig config;

    public Controller(KafkaTemplate<String, Candidate> kafkaTemplate, KafkaConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.config = config;
    }

    @PostMapping
    public String castVote(@RequestBody Candidate candidate) {
        logger.info("Casting vote for candidate {}", candidate.getName());
        kafkaTemplate.send(config.getTopic(), candidate);
        return "Vote casted successfully";
    }
}
