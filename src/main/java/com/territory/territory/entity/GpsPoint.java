package com.territory.territory.entity;

import java.time.Instant;
import java.util.UUID;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gps_points")
public class GpsPoint {

    @Id
    private UUID id;

    private UUID activityId;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private Instant recordedAt;
}