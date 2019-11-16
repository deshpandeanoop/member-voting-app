package com.github.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class VoteCountEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteCountEventPublisher.class);
    private final ObjectMapper objectMapper;
    private final TextWebSocketHandler voteCountWebSocketHandler;

    public VoteCountEventPublisher(ObjectMapper objectMapper, TextWebSocketHandler voteCountWebSocketHandler) {
        this.objectMapper = objectMapper;
        this.voteCountWebSocketHandler = voteCountWebSocketHandler;
    }

    public void publishEvent(final Candidate candidate){
        LOGGER.info("Received publish event for candidate {}", candidate.getName());
        try{
            voteCountWebSocketHandler.handleMessage(null, new TextMessage(objectMapper.writeValueAsBytes(candidate)));
        }catch(Exception ex){
            LOGGER.error("Cannot publish votes of candidate {}, Here is the exception : {}", candidate.getName(), ex.getMessage());
        }
    }
}