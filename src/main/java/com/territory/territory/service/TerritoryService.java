package com.territory.territory.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.territory.territory.repository.TerritoryRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class TerritoryService {

    private final TerritoryRepository territoryRepository;
    private final ObjectMapper objectMapper;

    public Map<String, Object> getUserTerritories(UUID userId) {

        List<Object[]> rows = territoryRepository.findUserTerritories(userId);
        List<Map<String, Object>> features = new ArrayList<>();

        for (Object[] row : rows) {
            UUID id = (UUID) row[0];
            Double area = (Double) row[1];
            String geoJsonStr = (String) row[2];

            Map<String, Object> geometry;
            try {
                geometry = objectMapper.readValue(geoJsonStr, Map.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse GeoJSON");
            }

            Map<String, Object> feature = new HashMap<>();

            feature.put("type", "Feature");

            Map<String, Object> properties = new HashMap<>();
            properties.put("territoryId", id);
            properties.put("area", area);

            feature.put("properties", properties);
            feature.put("geometry", geometry);

            features.add(feature);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("type", "FeatureCollection");
        response.put("features", features);

        return response;
    }
}