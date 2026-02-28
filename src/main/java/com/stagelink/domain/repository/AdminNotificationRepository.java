package com.stagelink.domain.repository;

import java.util.List;
import java.util.UUID;

import com.stagelink.domain.entity.AdminNotification;
import com.stagelink.domain.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNotificationRepository extends JpaRepository<AdminNotification, UUID> {

    List<AdminNotification> findByAdminIdOrderByCreatedAtDesc(UUID adminId);

    List<AdminNotification> findByAdminIdAndStatusOrderByCreatedAtDesc(UUID adminId, NotificationStatus status);

    List<AdminNotification> findByBookingRequestId(UUID bookingRequestId);
}
