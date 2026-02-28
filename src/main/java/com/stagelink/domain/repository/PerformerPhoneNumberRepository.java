package com.stagelink.domain.repository;

import java.util.List;
import java.util.UUID;

import com.stagelink.domain.entity.PerformerPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerPhoneNumberRepository extends JpaRepository<PerformerPhoneNumber, Long> {

    List<PerformerPhoneNumber> findByPerformerId(UUID performerId);
}
