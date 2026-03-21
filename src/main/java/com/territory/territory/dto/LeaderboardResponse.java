package com.territory.territory.dto;


public class LeaderboardResponse {

    private int rank;
    private String username;
    private Double totalArea;

    public LeaderboardResponse(int rank, String username, Double totalArea) {
        this.rank = rank;
        this.username = username;
        this.totalArea = totalArea;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public Double getTotalArea() {
        return totalArea;
    }
}