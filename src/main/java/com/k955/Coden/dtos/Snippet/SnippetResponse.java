package com.k955.Coden.dtos.Snippet;

import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;

import java.time.Instant;
import java.util.UUID;

public record SnippetResponse(
    UUID id,
    String title,
    String description,
    String code,
    Language language,
    Framework framework,
    SnippetType snippetType,
    SnippetStatus snippetStatus,
    UserProfileResponse createdBy,
    Instant createdAt
) {
}
