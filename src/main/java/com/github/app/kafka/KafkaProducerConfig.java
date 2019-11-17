package com.github.app.kafka;

import com.github.app.model.Candidate;
import com.github.app.model.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConfig.class);
    private final KafkaConfig config;

    public KafkaProducerConfig(KafkaConfig config) {
        this.config = config;
    }

    @Bean
    public ProducerFactory<String, Candidate> producerFactory(){
        Map<String,Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,config.getBootStrapServers());
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<String, Candidate> kafkaTemplate(){
        LOGGER.info("Instantiating KafkaTemplate");
        return new KafkaTemplate<>(producerFactory());
    }

}
