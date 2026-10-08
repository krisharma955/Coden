package com.k955.Coden.dtos.Bundle;

import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;

import java.time.Instant;
import java.util.UUID;

public record BundleResponse(
        UUID id,
        String name,
        String description,
        BundleStatus bundleStatus,
        BundleCategory bundleCategory,
        UserProfileResponse createdBy,
        Instant createdAt
) {
}
