package com.territory.territory.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.territory.territory.entity.Territory;

public interface TerritoryRepository extends JpaRepository<Territory, UUID> {

    @Query(value = """
    SELECT ST_Area(CAST(:polygon AS geography))
    """, nativeQuery = true)
    Double calculateArea(Object polygon);


    @Query(value = """
    SELECT 
        id,
        area,
        ST_AsGeoJSON(polygon) as geojson
    FROM territories
    WHERE user_id = :userId
    """, nativeQuery = true)
    List<Object[]> findUserTerritories(UUID userId);
}