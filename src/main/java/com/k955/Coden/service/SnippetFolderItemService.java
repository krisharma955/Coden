package com.k955.Coden.service;

import com.k955.Coden.dtos.SnippetFolderItem.SnippetFolderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SnippetFolderItemService {

    SnippetFolderItemResponse addSnippetToFolder(UUID folderId, UUID snippetId);

    Page<SnippetFolderItemResponse> getSnippetsFromFolder(UUID folderId, Pageable pageable);

    void removeSnippetFromFolder(UUID folderId, UUID snippetId);

}
