package com.stagelink.domain.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.stagelink.domain.enums.BookingRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_requests")
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private LocationSlot slot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performer_id", nullable = false)
    private Performer performer;

    @Column(name = "request_message", columnDefinition = "text")
    private String requestMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "booking_request_status")
    private BookingRequestStatus status = BookingRequestStatus.PENDING;

    @Column(name = "requested_at", nullable = false)
    private OffsetDateTime requestedAt;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_admin_id")
    private LocationAdmin reviewedByAdmin;

    @Column(name = "rejection_reason", columnDefinition = "text")
    private String rejectionReason;

    @OneToOne(mappedBy = "bookingRequest", fetch = FetchType.LAZY)
    private PublicEvent publicEvent;

    @OneToMany(mappedBy = "bookingRequest", fetch = FetchType.LAZY)
    private Set<AdminNotification> notifications = new HashSet<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocationSlot getSlot() { return slot; }
    public void setSlot(LocationSlot slot) { this.slot = slot; }
    public Performer getPerformer() { return performer; }
    public void setPerformer(Performer performer) { this.performer = performer; }
    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
    public BookingRequestStatus getStatus() { return status; }
    public void setStatus(BookingRequestStatus status) { this.status = status; }
    public OffsetDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(OffsetDateTime requestedAt) { this.requestedAt = requestedAt; }
    public OffsetDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(OffsetDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public LocationAdmin getReviewedByAdmin() { return reviewedByAdmin; }
    public void setReviewedByAdmin(LocationAdmin reviewedByAdmin) { this.reviewedByAdmin = reviewedByAdmin; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public PublicEvent getPublicEvent() { return publicEvent; }
    public void setPublicEvent(PublicEvent publicEvent) { this.publicEvent = publicEvent; }
    public Set<AdminNotification> getNotifications() { return notifications; }
    public void setNotifications(Set<AdminNotification> notifications) { this.notifications = notifications; }
}
