package com.k955.Coden.dtos.Snippet;

import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;

import java.util.UUID;

public record SnippetView(
        UUID id,
        String title,
        Language language,
        Framework framework,
        SnippetType snippetType,
        SnippetStatus snippetStatus
) {
}
