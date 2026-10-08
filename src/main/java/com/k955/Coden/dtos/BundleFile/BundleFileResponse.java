package com.k955.Coden.dtos.BundleFile;

import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.enums.Common.Language;

import java.time.Instant;
import java.util.UUID;

public record BundleFileResponse(
        UUID id,
        String fileName,
        String filePath,
        long size,
        Language language,
        String extension,
        BundleResponse bundle,
        UserProfileResponse uploadedBy,
        Instant createdAt
) {
}
