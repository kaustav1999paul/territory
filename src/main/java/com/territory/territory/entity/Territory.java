package com.territory.territory.entity;

import java.time.Instant;
import java.util.UUID;

import org.locationtech.jts.geom.Polygon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "territories")
public class Territory {

    @Id
    private UUID id;

    private UUID userId;

    private UUID activityId;

    private Double area;

    @Column(columnDefinition = "geometry(Polygon,4326)")
    private Polygon polygon;

    private Instant createdAt;
}