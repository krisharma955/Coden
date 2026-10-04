package com.k955.Coden.dtos.Snippet;

import com.k955.Coden.enums.Snippet.SnippetStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSnippetStatus(
        @NotNull SnippetStatus snippetStatus
) {
}
