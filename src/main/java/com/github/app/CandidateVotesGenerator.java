package com.github.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.app.model.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates votes.txt in /src/main/resources/data/ directory.
 * Use the file for pumping data into kafka using kafka-console-producer utility.
 */
public class CandidateVotesGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateVotesGenerator.class);
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    public static void main(String[] args) throws Exception{
        LOGGER.info("Creating mock votes data");
        final String filePath = "./src/main/resources/data/votes.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
        List<Candidate> candidates = new ArrayList<>();
        candidates.addAll(createCandidatesList("candidate1",100));
        candidates.addAll(createCandidatesList("candidate2",100));
        candidates.addAll(createCandidatesList("candidate3",100));
        for(Candidate candidate : candidates){
            bufferedWriter.write(objectMapper.writeValueAsString(candidate));
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        LOGGER.info("Dataset has been successfully generated at {}", filePath);
    }

    private static List<Candidate> createCandidatesList(final String candidateName, int listSize){
        List<Candidate> candidates = new ArrayList<>();
        for(int index = 0 ; index < listSize ; ++index){
            candidates.add(createCandidate(candidateName));
        }
        return candidates;
     }

     private static Candidate createCandidate(String name){
         Candidate candidate = new Candidate();
         candidate.setName(name);
         candidate.setVotes(new Random().nextInt(20) +1);
         return candidate;
     }

}
