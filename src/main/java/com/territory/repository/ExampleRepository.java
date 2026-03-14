package com.territory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.territory.entity.ExampleEntity;

public interface ExampleRepository
        extends JpaRepository<ExampleEntity, Long> {
}