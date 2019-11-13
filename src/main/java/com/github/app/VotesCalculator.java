package com.github.app;

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
public class VotesCalculator implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(VotesCalculator.class);
    private VotesCalculateTask votesCalculateTask;

    @Override
    public void run(String... args) throws Exception {
        try(KafkaStreams kafkaStreams = new KafkaStreams(topology(args), configuration(args))){
            logger.info("Starting kafka stream processing");
            kafkaStreams.start();
        }
    }

    private Properties configuration(String... args){
        logger.info("Building configuration properties for kafka stream processing");
        logger.debug("Received command line arguments, {}", args);
        Properties configuration = new Properties();
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, args[0]);
        configuration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, args[1]);
        configuration.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE); //Exactly once semantics
        return configuration;
    }

    private Topology topology(String... args){
        logger.info("Building topology for votes counting");
        logger.info("Received command line arguments, {}", args.length);
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Long, String> votesStream = streamsBuilder.stream("gs-votes", Consumed.with(Serdes.Long(), Serdes.String()));
        votesStream.foreach(votesCalculateTask);
        return streamsBuilder.build();
    }

    @Autowired
    public void setVotesCalculateTask(VotesCalculateTask votesCalculateTask) {
        this.votesCalculateTask = votesCalculateTask;
    }
}