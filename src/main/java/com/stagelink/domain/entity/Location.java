package com.stagelink.domain.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(name = "google_maps_link", columnDefinition = "text")
    private String googleMapsLink;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 80)
    private String timezone = "UTC";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private Set<LocationPhoneNumber> phoneNumbers = new HashSet<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private Set<LocationAdmin> admins = new HashSet<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private Set<LocationSlot> slots = new HashSet<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private Set<PublicEvent> publicEvents = new HashSet<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGoogleMapsLink() { return googleMapsLink; }
    public void setGoogleMapsLink(String googleMapsLink) { this.googleMapsLink = googleMapsLink; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Set<LocationPhoneNumber> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(Set<LocationPhoneNumber> phoneNumbers) { this.phoneNumbers = phoneNumbers; }
    public Set<LocationAdmin> getAdmins() { return admins; }
    public void setAdmins(Set<LocationAdmin> admins) { this.admins = admins; }
    public Set<LocationSlot> getSlots() { return slots; }
    public void setSlots(Set<LocationSlot> slots) { this.slots = slots; }
    public Set<PublicEvent> getPublicEvents() { return publicEvents; }
    public void setPublicEvents(Set<PublicEvent> publicEvents) { this.publicEvents = publicEvents; }
}
