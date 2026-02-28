package com.stagelink.auth.service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Set;

import com.stagelink.auth.dto.AuthResponse;
import com.stagelink.auth.dto.LoginRequest;
import com.stagelink.auth.dto.RegisterRequest;
import com.stagelink.auth.entity.AppUser;
import com.stagelink.auth.enums.AppRole;
import com.stagelink.auth.repository.AppUserRepository;
import com.stagelink.auth.security.JwtService;
import com.stagelink.domain.entity.Location;
import com.stagelink.domain.entity.LocationAdmin;
import com.stagelink.domain.entity.Performer;
import com.stagelink.domain.repository.LocationAdminRepository;
import com.stagelink.domain.repository.LocationRepository;
import com.stagelink.domain.repository.PerformerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final Set<AppRole> SUPPORTED_SELF_AUTH_ROLES =
        EnumSet.of(AppRole.PERFORMER, AppRole.LOCATION_ADMIN);

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PerformerRepository performerRepository;
    private final LocationRepository locationRepository;
    private final LocationAdminRepository locationAdminRepository;

    public AuthService(
        AppUserRepository appUserRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        PerformerRepository performerRepository,
        LocationRepository locationRepository,
        LocationAdminRepository locationAdminRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.performerRepository = performerRepository;
        this.locationRepository = locationRepository;
        this.locationAdminRepository = locationAdminRepository;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateSupportedRole(request.getRole(), "registration");

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser user = new AppUser();
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setRoles(Set.of(request.getRole()));
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        AppUser savedUser = appUserRepository.save(user);
        createLinkedProfile(savedUser, request);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getFullName(), savedUser.getRoles());
    }

    public AuthResponse login(LoginRequest request) {
        validateSupportedRole(request.getRole(), "login");

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        AppUser user = appUserRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!user.getRoles().contains(request.getRole())) {
            throw new BadCredentialsException("User does not have requested role");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRoles());
    }

    private void createLinkedProfile(AppUser appUser, RegisterRequest request) {
        if (request.getRole() == AppRole.PERFORMER) {
            createPerformerProfile(appUser, request);
            return;
        }

        if (request.getRole() == AppRole.LOCATION_ADMIN) {
            createLocationAdminProfile(appUser, request);
            return;
        }

        throw new IllegalArgumentException("Unsupported role for profile creation");
    }

    private void createPerformerProfile(AppUser appUser, RegisterRequest request) {
        int members = request.getPerformerNumberOfMembers() == null ? 1 : request.getPerformerNumberOfMembers();
        if (members <= 0) {
            throw new IllegalArgumentException("performerNumberOfMembers must be greater than 0");
        }

        Performer performer = new Performer();
        performer.setAppUser(appUser);
        performer.setTitle(request.getPerformerTitle());
        performer.setName(request.getFullName().trim());
        performer.setDescription(request.getPerformerDescription());
        performer.setSpecialRequirements(request.getPerformerSpecialRequirements());
        performer.setNumberOfMembers(members);
        performer.setCreatedAt(OffsetDateTime.now());
        performer.setUpdatedAt(OffsetDateTime.now());
        performerRepository.save(performer);
    }

    private void createLocationAdminProfile(AppUser appUser, RegisterRequest request) {
        if (request.getLocationId() == null) {
            throw new IllegalArgumentException("locationId is required for LOCATION_ADMIN registration");
        }

        Location location = locationRepository.findById(request.getLocationId())
            .orElseThrow(() -> new IllegalArgumentException("Location not found for provided locationId"));

        LocationAdmin locationAdmin = new LocationAdmin();
        locationAdmin.setAppUser(appUser);
        locationAdmin.setLocation(location);
        locationAdmin.setFullName(request.getFullName().trim());
        locationAdmin.setPhoneNumber(request.getPhoneNumber());
        locationAdmin.setCreatedAt(OffsetDateTime.now());
        locationAdminRepository.save(locationAdmin);
    }

    private void validateSupportedRole(AppRole role, String operation) {
        if (!SUPPORTED_SELF_AUTH_ROLES.contains(role)) {
            throw new IllegalArgumentException(
                "Unsupported role for " + operation + ". Allowed roles: LOCATION_ADMIN, PERFORMER"
            );
        }
    }
}
