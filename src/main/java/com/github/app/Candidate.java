package com.github.app;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Candidate {
    private String name;
    private Integer votes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalVotes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }
}
