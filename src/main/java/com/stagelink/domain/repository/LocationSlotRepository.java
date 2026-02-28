package com.stagelink.domain.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.stagelink.domain.entity.LocationSlot;
import com.stagelink.domain.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationSlotRepository extends JpaRepository<LocationSlot, UUID> {

    List<LocationSlot> findByLocationIdOrderByStartsAtAsc(UUID locationId);

    List<LocationSlot> findByLocationIdAndStartsAtGreaterThanEqualAndEndsAtLessThanEqualOrderByStartsAtAsc(
        UUID locationId,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
    );

    List<LocationSlot> findByLocationIdAndStatus(UUID locationId, SlotStatus status);
}
