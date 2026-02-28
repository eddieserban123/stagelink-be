package com.stagelink.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.stagelink.domain.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerRepository extends JpaRepository<Performer, UUID> {
    Optional<Performer> findByAppUserId(UUID appUserId);
}
