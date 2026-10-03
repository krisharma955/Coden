package com.k955.Coden.dtos.Snippet;

import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetType;

public record UpdateSnippetRequest(
        String title,
        String description,
        String code,
        Language language,
        Framework framework,
        SnippetType snippetType
) {
}
