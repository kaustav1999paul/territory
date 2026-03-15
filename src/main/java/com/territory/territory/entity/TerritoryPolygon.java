package com.territory.territory.entity;

import java.util.UUID;

import org.locationtech.jts.geom.Polygon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TerritoryPolygon {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "geometry(Polygon,4326)")
    private Polygon polygon;

}