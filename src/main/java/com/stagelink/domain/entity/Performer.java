package com.stagelink.domain.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.stagelink.auth.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "performers")
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", unique = true)
    private AppUser appUser;

    @Column(length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "number_of_members", nullable = false)
    private Integer numberOfMembers = 1;

    @Column(name = "special_requirements", columnDefinition = "text")
    private String specialRequirements;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "performer", fetch = FetchType.LAZY)
    private Set<PerformerPhoneNumber> phoneNumbers = new HashSet<>();

    @OneToMany(mappedBy = "performer", fetch = FetchType.LAZY)
    private Set<BookingRequest> bookingRequests = new HashSet<>();

    @OneToMany(mappedBy = "performer", fetch = FetchType.LAZY)
    private Set<PublicEvent> publicEvents = new HashSet<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getNumberOfMembers() { return numberOfMembers; }
    public void setNumberOfMembers(Integer numberOfMembers) { this.numberOfMembers = numberOfMembers; }
    public String getSpecialRequirements() { return specialRequirements; }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Set<PerformerPhoneNumber> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(Set<PerformerPhoneNumber> phoneNumbers) { this.phoneNumbers = phoneNumbers; }
    public Set<BookingRequest> getBookingRequests() { return bookingRequests; }
    public void setBookingRequests(Set<BookingRequest> bookingRequests) { this.bookingRequests = bookingRequests; }
    public Set<PublicEvent> getPublicEvents() { return publicEvents; }
    public void setPublicEvents(Set<PublicEvent> publicEvents) { this.publicEvents = publicEvents; }
}
