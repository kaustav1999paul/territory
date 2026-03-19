package com.territory.territory.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.territory.territory.service.ActivityService;
import com.territory.territory.util.JwtUtil;
import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.dto.CompleteActivityRequest;
import com.territory.territory.dto.CompleteActivityResponse;

import java.util.UUID;

import com.territory.territory.dto.ActivityStartResponse;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService service;
    private final JwtUtil jwtUtil;

    public ActivityController(ActivityService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/start")
    public ApiResponse<ActivityStartResponse> start(
            @RequestHeader("Authorization") String header
    ) {

        String username = jwtUtil.extractUsername(header.substring(7));

        UUID userId = UUID.nameUUIDFromBytes(username.getBytes());

        UUID activityId = service.startActivity(userId);

        return ApiResponse.success(new ActivityStartResponse(activityId));
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<CompleteActivityResponse> complete(
            @RequestHeader("Authorization") String header,
            @PathVariable UUID id,
            @Valid @RequestBody CompleteActivityRequest request
    ) {

        String username = jwtUtil.extractUsername(header.substring(7));
        UUID userId = UUID.nameUUIDFromBytes(username.getBytes());

        return ApiResponse.success(
                service.completeActivity(userId, id, request.getPoints())
        );
    }
}