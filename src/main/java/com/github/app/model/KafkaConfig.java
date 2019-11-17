package com.github.app.model;

public class KafkaConfig {
    private String bootStrapServers;
    private String applicationId;
    private String topic;

    public String getBootStrapServers() {
        return bootStrapServers;
    }

    public void setBootStrapServers(String bootStrapServers) {
        this.bootStrapServers = bootStrapServers;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
