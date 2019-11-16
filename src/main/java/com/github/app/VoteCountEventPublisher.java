package com.github.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Component
public class VoteCountEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteCountEventPublisher.class);
    private final List<SseEmitter> subscribers;
    private final ObjectMapper objectMapper;

    public VoteCountEventPublisher(List<SseEmitter> subscribers, ObjectMapper objectMapper) {
        this.subscribers = subscribers;
        this.objectMapper = objectMapper;
    }

    public void publishEvent(final Candidate candidate){
        LOGGER.info("Received publish event for candidate {}", candidate.getName());
        subscribers.forEach(subscriber ->{
            try{
                subscriber.send(SseEmitter.event().name("votes").data(objectMapper.writeValueAsString(candidate)));
            }catch(Exception ex){
                LOGGER.error("Cannot send event for candidate {} for subscriber {}. Here is the exception message {}."
                        , candidate.getName(), subscriber, ex.getMessage());
            }
        });
    }
}
