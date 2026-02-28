package com.stagelink.auth.dto;

import java.util.Set;
import java.util.UUID;

import com.stagelink.auth.enums.AppRole;

public class AuthResponse {

    private String accessToken;
    private UUID userId;
    private String email;
    private String fullName;
    private Set<AppRole> roles;

    public AuthResponse(String accessToken, UUID userId, String email, String fullName, Set<AppRole> roles) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
    }

    public String getAccessToken() { return accessToken; }
    public UUID getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public Set<AppRole> getRoles() { return roles; }
}
