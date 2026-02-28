package com.stagelink.domain.repository;

import java.util.List;
import java.util.UUID;

import com.stagelink.domain.entity.LocationPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationPhoneNumberRepository extends JpaRepository<LocationPhoneNumber, Long> {

    List<LocationPhoneNumber> findByLocationId(UUID locationId);
}
