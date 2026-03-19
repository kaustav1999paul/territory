package com.territory.territory.dto;

public record CompleteActivityResponse(
        boolean loopClosed,
        double distanceFromStart,
        double area
) {}
