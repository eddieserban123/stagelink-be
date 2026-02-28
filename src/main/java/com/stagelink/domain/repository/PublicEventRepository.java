package com.stagelink.domain.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.stagelink.domain.entity.PublicEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicEventRepository extends JpaRepository<PublicEvent, UUID> {

    List<PublicEvent> findByLocationIdOrderByPublishedAtDesc(UUID locationId);

    List<PublicEvent> findByPerformerIdOrderByPublishedAtDesc(UUID performerId);

    List<PublicEvent> findByLocationIdAndPublishedAtBetweenOrderByPublishedAtAsc(
        UUID locationId,
        OffsetDateTime from,
        OffsetDateTime to
    );

    Optional<PublicEvent> findBySlotId(UUID slotId);
}
