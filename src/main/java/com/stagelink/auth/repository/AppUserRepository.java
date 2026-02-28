package com.stagelink.auth.repository;

import java.util.Optional;
import java.util.UUID;

import com.stagelink.auth.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
