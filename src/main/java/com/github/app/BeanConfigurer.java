package com.github.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class BeanConfigurer {
    private static Logger logger = LoggerFactory.getLogger(BeanConfigurer.class);

    @Bean
    public List<SseEmitter> subscribers(){
        logger.info("Instantiating subscribers");
        return new CopyOnWriteArrayList<>();
    }

    @Bean
    public ObjectMapper objectMapper(){
        logger.info("Instantiating object mapper");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
