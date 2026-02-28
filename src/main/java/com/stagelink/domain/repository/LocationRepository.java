package com.stagelink.domain.repository;

import java.util.UUID;

import com.stagelink.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
