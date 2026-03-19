package com.territory.territory.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.dto.ActivityStartResponse;
import com.territory.territory.dto.CompleteActivityRequest;
import com.territory.territory.dto.CompleteActivityResponse;
import com.territory.territory.security.JwtUtil;
import com.territory.territory.service.ActivityService;

import jakarta.validation.Valid;

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

        String userIdStr = jwtUtil.extractUserId(header.substring(7));
        UUID userId = UUID.fromString(userIdStr);
        UUID activityId = service.startActivity(userId);

        return ApiResponse.success(new ActivityStartResponse(activityId));
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<CompleteActivityResponse> complete(
            @RequestHeader("Authorization") String header,
            @PathVariable UUID id,
            @Valid @RequestBody CompleteActivityRequest request
    ) {

        String userIdStr = jwtUtil.extractUserId(header.substring(7));
        UUID userId = UUID.fromString(userIdStr);

        return ApiResponse.success(
                service.completeActivity(userId, id, request.getPoints())
        );
    }
}