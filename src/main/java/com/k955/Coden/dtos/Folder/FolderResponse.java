package com.k955.Coden.dtos.Folder;

import com.k955.Coden.dtos.User.UserProfileResponse;

import java.time.Instant;
import java.util.UUID;

public record FolderResponse(
        UUID id,
        String name,
        String description,
        UserProfileResponse user,
        Instant createdAt
) {
}
