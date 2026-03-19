package com.territory.territory.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public class GpsPointDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Instant timestamp;

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Instant getTimestamp() { return timestamp; }
}