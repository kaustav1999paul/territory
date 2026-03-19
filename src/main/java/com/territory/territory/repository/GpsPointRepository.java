package com.territory.territory.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.territory.territory.entity.GpsPoint;

public interface GpsPointRepository extends JpaRepository<GpsPoint, UUID> {}
