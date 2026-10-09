package com.k955.Coden.dtos.User;

import com.k955.Coden.enums.User.Role;

import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String name,
        String username,
        String email,
        Role role,
        String avatarUrl,
        String profileUrl,
        Instant createdAt
) {
}
