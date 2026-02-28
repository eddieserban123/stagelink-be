package com.stagelink.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.stagelink.domain.entity.LocationAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationAdminRepository extends JpaRepository<LocationAdmin, UUID> {

    List<LocationAdmin> findByLocationId(UUID locationId);
    Optional<LocationAdmin> findByAppUserId(UUID appUserId);
}
