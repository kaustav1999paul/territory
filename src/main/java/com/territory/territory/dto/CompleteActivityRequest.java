package com.territory.territory.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class CompleteActivityRequest {

    @NotEmpty
    private List<GpsPointDto> points;

    public List<GpsPointDto> getPoints() {
        return points;
    }
}