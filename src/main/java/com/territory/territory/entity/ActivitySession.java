package com.territory.territory.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activity_sessions")
public class ActivitySession {

    @Id
    private UUID id;

    private UUID userId;

    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;
}
