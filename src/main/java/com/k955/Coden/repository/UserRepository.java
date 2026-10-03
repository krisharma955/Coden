package com.k955.Coden.repository;

import com.k955.Coden.entity.User;
import com.k955.Coden.enums.User.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<Role> findRoleById(UUID userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank String email);

}
