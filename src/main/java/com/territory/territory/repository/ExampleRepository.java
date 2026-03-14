package com.territory.territory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.territory.territory.entity.ExampleEntity;

public interface ExampleRepository
        extends JpaRepository<ExampleEntity, Long> {
}