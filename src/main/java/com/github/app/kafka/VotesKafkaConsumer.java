package com.github.app.kafka;

import com.github.app.model.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class VotesKafkaConsumer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(VotesKafkaConsumer.class);
    private VotesProcessor votesCalculateTask;
    private final KafkaConfig config;

    public VotesKafkaConsumer(KafkaConfig config) {
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Starting kafka stream processing");
        new KafkaStreams(topology(), configuration()).start();
    }

    private Properties configuration(){
        LOGGER.info("Building configuration properties for kafka stream processing");
        Properties configuration = new Properties();
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, config.getApplicationId());
        configuration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootStrapServers());
        configuration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        configuration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return configuration;
    }

    private Topology topology(){
        LOGGER.info("Building topology for votes counting");
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Long, String> votesStream = streamsBuilder.stream(config.getTopic(), Consumed.with(Serdes.Long(), Serdes.String()));
        votesStream.foreach(votesCalculateTask);
        return streamsBuilder.build();
    }

    @Autowired
    public void setVotesCalculateTask(VotesProcessor votesCalculateTask) {
        this.votesCalculateTask = votesCalculateTask;
    }
}