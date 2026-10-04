package com.k955.Coden.dtos.Folder;

import jakarta.validation.constraints.NotBlank;

public record FolderRequest(
        @NotBlank String name,
        String description
) {
}
