package com.stagelink.auth.dto;

import com.stagelink.auth.enums.AppRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotNull
    private AppRole role;

    @Size(max = 100)
    private String performerTitle;

    private String performerDescription;

    private Integer performerNumberOfMembers;

    private String performerSpecialRequirements;

    @Size(max = 40)
    private String phoneNumber;

    private UUID locationId;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public AppRole getRole() { return role; }
    public void setRole(AppRole role) { this.role = role; }
    public String getPerformerTitle() { return performerTitle; }
    public void setPerformerTitle(String performerTitle) { this.performerTitle = performerTitle; }
    public String getPerformerDescription() { return performerDescription; }
    public void setPerformerDescription(String performerDescription) { this.performerDescription = performerDescription; }
    public Integer getPerformerNumberOfMembers() { return performerNumberOfMembers; }
    public void setPerformerNumberOfMembers(Integer performerNumberOfMembers) { this.performerNumberOfMembers = performerNumberOfMembers; }
    public String getPerformerSpecialRequirements() { return performerSpecialRequirements; }
    public void setPerformerSpecialRequirements(String performerSpecialRequirements) { this.performerSpecialRequirements = performerSpecialRequirements; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
}
