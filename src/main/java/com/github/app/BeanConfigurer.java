package com.github.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.app.model.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfigurer.class);
    private ApplicationArguments applicationArguments;

    public BeanConfigurer(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Bean
    public ObjectMapper objectMapper(){
        LOGGER.info("Instantiating object mapper");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public KafkaConfig kafkaConfig(){
        KafkaConfig config = new KafkaConfig();
        config.setApplicationId(applicationArguments.getSourceArgs()[0]);
        config.setBootStrapServers(applicationArguments.getSourceArgs()[1]);
        config.setTopic(applicationArguments.getSourceArgs()[2]);
        return config;
    }
}
