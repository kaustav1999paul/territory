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
        id, area, ST_AsGeoJSON(polygon) as geojson
        FROM territories
        WHERE user_id = :userId
    """, nativeQuery = true)
    List<Object[]> findUserTerritories(UUID userId);


    @Query(value = """
        SELECT id, user_id, polygon
        FROM territories
        WHERE ST_Intersects(polygon, CAST(:polygon AS geometry))
    """, nativeQuery = true)
    List<Object[]> findIntersectingTerritories(Object polygon);


    @Query(value = """
        SELECT ST_AsBinary(ST_Difference(
        CAST(:existing AS geometry),
        CAST(:incoming AS geometry)
        ))
    """, nativeQuery = true)
    byte[] subtractGeometry(Object existing, Object incoming);


    @Query("""
        SELECT u.username, SUM(t.area)
        FROM Territory t
        JOIN User u ON t.userId = u.id
        GROUP BY u.username
        ORDER BY SUM(t.area) DESC
    """)
    List<Object[]> getLeaderboard();
    
}