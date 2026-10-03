package com.k955.Coden.dtos.Snippet;

import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SnippetRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String code,
        @NotNull Language language,
        Framework framework,
        @NotNull SnippetType snippetType
) {
}
