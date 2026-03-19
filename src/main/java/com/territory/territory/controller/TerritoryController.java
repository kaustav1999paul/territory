package com.territory.territory.controller;


import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.territory.common.api.ApiResponse;
import com.territory.territory.service.TerritoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/territories")
@RequiredArgsConstructor
public class TerritoryController {

    private final TerritoryService territoryService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getUserTerritories(
            @RequestAttribute("userId") UUID userId
    ) {
        return ApiResponse.success(
                territoryService.getUserTerritories(userId)
        );
    }
}