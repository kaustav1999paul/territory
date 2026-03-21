package com.territory.territory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.dto.LeaderboardResponse;
import com.territory.territory.service.LeaderboardService;

@RestController
public class LeaderboardController {

    private final LeaderboardService service;

    public LeaderboardController(LeaderboardService service) {
        this.service = service;
    }

    @GetMapping("/leaderboard")
    public ApiResponse<List<LeaderboardResponse>> getLeaderboard() {
        return ApiResponse.success(service.getLeaderboard());
    }
}