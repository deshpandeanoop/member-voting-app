package com.github.app.kafka;

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
    private static final Logger logger = LoggerFactory.getLogger(VotesKafkaConsumer.class);
    private VotesProcessor votesCalculateTask;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting kafka stream processing");
        new KafkaStreams(topology(args), configuration(args)).start();
    }

    private Properties configuration(String... args){
        logger.info("Building configuration properties for kafka stream processing");
        logger.debug("Received command line arguments, {}", args);
        Properties configuration = new Properties();
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, args[0]);
        configuration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, args[1]);
        configuration.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE); //Exactly once semantics
        configuration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        configuration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return configuration;
    }

    private Topology topology(String... args){
        logger.info("Building topology for votes counting");
        logger.info("Received command line arguments, {}", args.length);
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Long, String> votesStream = streamsBuilder.stream(args[2], Consumed.with(Serdes.Long(), Serdes.String()));
        votesStream.foreach(votesCalculateTask);
        return streamsBuilder.build();
    }

    @Autowired
    public void setVotesCalculateTask(VotesProcessor votesCalculateTask) {
        this.votesCalculateTask = votesCalculateTask;
    }
}