# member-voting-app
 Member voting application using event sourcing and in memory as current application state.
 
 Software requirements : Java 8, Kafka (confluent platform is preferred)

Steps :

1)Start kafka {confluent}/bin/confluent start kafka

2)Create topic {confluent}/bin/kafka-topics --zookeeper localhost:2181 --create --topic votes-topic --partitions 1 --replication-factor 1

3)Import as maven project and run as spring boot application. Pass application-id, bootstrap-servers, kafka-topic-name as command line arguments (Ex: voting-application localhost:9092 votes-topic). UI can be launched at http:localhost:8080/index.html


REST endpoints:

http:localhost:8080/votes

 GET - Returns total votes of candidates(For keeping application simple, fixed candidate count to 3).
 
 POST - Pass JSON payload with candidate's name and number of votes.
 
      Example payload : {
      
                         "name" : "candidate1",
                         
                         "votes" : 10
                         
                        }
                        
 Allowable candidate names {"candidate1", "candidate2", "candidate3"}.
 
For bulk input loads, run CandidateVotesGenerator, which generates file with votes data. Feed in the generated file using kafka-console-producer utility.

Note: Before boot-strapping spring boot app, go to confluent's directory and run

./bin/kafka-streams-application-reset --application-id voting-application(passed in command line argument) --to-earliest --input-topics topic-name

