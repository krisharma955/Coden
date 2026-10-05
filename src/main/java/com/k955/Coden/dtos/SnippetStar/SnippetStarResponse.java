package com.k955.Coden.dtos.SnippetStar;

import com.k955.Coden.dtos.Snippet.SnippetView;
import com.k955.Coden.dtos.User.UserProfileResponse;

import java.time.Instant;
import java.util.UUID;

public record SnippetStarResponse(
        UUID id,
        UserProfileResponse user,
        SnippetView snippet,
        Instant starredAt
) {
}