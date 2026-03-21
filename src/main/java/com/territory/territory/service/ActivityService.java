package com.territory.territory.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKBReader;
import org.springframework.stereotype.Service;

import com.territory.territory.dto.CompleteActivityResponse;
import com.territory.territory.dto.GpsPointDto;
import com.territory.territory.entity.ActivitySession;
import com.territory.territory.entity.ActivityStatus;
import com.territory.territory.entity.GpsPoint;
import com.territory.territory.entity.Territory;
import com.territory.territory.repository.ActivityRepository;
import com.territory.territory.repository.GpsPointRepository;
import com.territory.territory.repository.TerritoryRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepo;
    private final GpsPointRepository gpsRepo;
    private final TerritoryRepository territoryRepo;

    private final GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
    private final WKBReader wkbReader = new WKBReader();

    public ActivityService(
            ActivityRepository activityRepo,
            GpsPointRepository gpsRepo,
            TerritoryRepository territoryRepo
    ) {
        this.activityRepo = activityRepo;
        this.gpsRepo = gpsRepo;
        this.territoryRepo = territoryRepo;
    }

    public UUID startActivity(UUID userId) {

        ActivitySession session = new ActivitySession();
        session.setId(UUID.randomUUID());
        session.setUserId(userId);
        session.setStartedAt(Instant.now());
        session.setStatus(ActivityStatus.ONGOING);

        activityRepo.save(session);

        return session.getId();
    }

    public CompleteActivityResponse completeActivity(
            UUID userId,
            UUID activityId,
            List<GpsPointDto> points
    ) {

        //  Basic validation
        if (points.size() < 10) {
            throw new RuntimeException("Not enough points to form loop");
        }

        GpsPointDto first = points.get(0);
        GpsPointDto last = points.get(points.size() - 1);

        double distance = distanceMeters(
                first.getLatitude(), first.getLongitude(),
                last.getLatitude(), last.getLongitude()
        );

        if (distance > 20) {
            throw new RuntimeException("Loop not closed. Distance: " + distance + " meters");
        }

        //  Save GPS points
        List<Coordinate> coordinates = new ArrayList<>();

        for (GpsPointDto dto : points) {

            Point point = factory.createPoint(
                    new Coordinate(dto.getLongitude(), dto.getLatitude())
            );

            GpsPoint entity = new GpsPoint();
            entity.setId(UUID.randomUUID());
            entity.setActivityId(activityId);
            entity.setLocation(point);
            entity.setRecordedAt(dto.getTimestamp());

            gpsRepo.save(entity);

            coordinates.add(point.getCoordinate());
        }

        //  Close polygon
        coordinates.add(coordinates.get(0));

        Polygon polygon = factory.createPolygon(
                coordinates.toArray(new Coordinate[0])
        );

        //  Area validation
        Double area = territoryRepo.calculateArea(polygon);

        if (area == null || area < 100) {
            throw new IllegalArgumentException("Area too small. Minimum 100 sq meters required");
        }

        // =========================================================
        //  TERRITORY CUTTING LOGIC (CORE GAME ENGINE)
        // =========================================================

        List<Object[]> overlaps = territoryRepo.findIntersectingTerritories(polygon);

        for (Object[] row : overlaps) {

            UUID existingId = (UUID) row[0];
            UUID ownerId = (UUID) row[1];
            Object existingPolygon = row[2];

            byte[] diffWkb = territoryRepo.subtractGeometry(existingPolygon, polygon);

            if (diffWkb == null) continue;

            Geometry geom;
            try {
                geom = wkbReader.read(diffWkb);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse geometry");
            }

            //  Fully eaten → delete
            if (geom.isEmpty()) {
                territoryRepo.deleteById(existingId);
                continue;
            }

            //  Handle Polygon / MultiPolygon
            List<Polygon> polygons = new ArrayList<>();

            if (geom instanceof Polygon) {
                polygons.add((Polygon) geom);
            } else if (geom instanceof MultiPolygon multi) {
                for (int i = 0; i < multi.getNumGeometries(); i++) {
                    polygons.add((Polygon) multi.getGeometryN(i));
                }
            }

            //  Delete original (we will recreate pieces)
            territoryRepo.deleteById(existingId);

            for (Polygon p : polygons) {

                Double newArea = territoryRepo.calculateArea(p);

                if (newArea == null || newArea < 50) continue; // ignore tiny fragments

                Territory updated = new Territory();
                updated.setId(UUID.randomUUID());
                updated.setUserId(ownerId);
                updated.setActivityId(null);
                updated.setPolygon(p);
                updated.setArea(newArea);
                updated.setCreatedAt(Instant.now());

                territoryRepo.save(updated);
            }
        }

        // =========================================================
        //  SAVE NEW TERRITORY
        // =========================================================

        Territory newTerritory = new Territory();

        newTerritory.setId(UUID.randomUUID());
        newTerritory.setUserId(userId);
        newTerritory.setActivityId(activityId);
        newTerritory.setPolygon(polygon);
        newTerritory.setArea(area);
        newTerritory.setCreatedAt(Instant.now());

        territoryRepo.save(newTerritory);

        // =========================================================
        //  COMPLETE ACTIVITY
        // =========================================================

        ActivitySession session = activityRepo.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (session.getStatus() == ActivityStatus.COMPLETED) {
            throw new IllegalStateException("Activity already completed");
        }

        session.setEndedAt(Instant.now());
        session.setStatus(ActivityStatus.COMPLETED);

        activityRepo.save(session);

        return new CompleteActivityResponse(true, distance, area);
    }

    // =========================================================
    //  DISTANCE CALCULATION
    // =========================================================

    private double distanceMeters(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371000;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) *
                                Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}