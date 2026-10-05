package com.k955.Coden.dtos.SnippetFolderItem;

import com.k955.Coden.dtos.Folder.FolderResponse;
import com.k955.Coden.dtos.Snippet.SnippetView;

import java.time.Instant;
import java.util.UUID;

public record SnippetFolderItemResponse(
        UUID id,
        SnippetView snippet,
        FolderResponse folder,
        Instant addedAt
) {
}
