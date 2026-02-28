package com.stagelink.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.stagelink.domain.entity.BookingRequest;
import com.stagelink.domain.enums.BookingRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, UUID> {

    List<BookingRequest> findBySlotId(UUID slotId);

    List<BookingRequest> findBySlotIdAndStatus(UUID slotId, BookingRequestStatus status);

    List<BookingRequest> findByPerformerId(UUID performerId);

    Optional<BookingRequest> findBySlotIdAndPerformerId(UUID slotId, UUID performerId);

}
