package com.territory.territory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.territory.territory.dto.LeaderboardResponse;
import com.territory.territory.repository.TerritoryRepository;

@Service
public class LeaderboardService {

    private final TerritoryRepository territoryRepo;

    public LeaderboardService(TerritoryRepository territoryRepo) {
        this.territoryRepo = territoryRepo;
    }

    
    public List<LeaderboardResponse> getLeaderboard() {
        List<Object[]> results = territoryRepo.getLeaderboard();
        List<LeaderboardResponse> response = new ArrayList<>();
        int rank = 1;

        for (Object[] row : results) {
            String username = (String) row[0];
            Double totalArea = (Double) row[1];
            response.add(new LeaderboardResponse(rank++, username, totalArea));
        }
        return response;
    }
}