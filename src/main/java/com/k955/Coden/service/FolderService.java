package com.k955.Coden.service;

import com.k955.Coden.dtos.Folder.FolderRequest;
import com.k955.Coden.dtos.Folder.FolderResponse;
import com.k955.Coden.dtos.Folder.UpdateFolderRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FolderService {

    FolderResponse createFolder(@Valid FolderRequest folderRequest);

    FolderResponse getFolderById(UUID folderId);

    Page<FolderResponse> getFolders(String search, Pageable pageable);

    FolderResponse updateFolder(UUID folderId, UpdateFolderRequest updateFolderRequest);

    void deleteFolder(UUID folderId);

}
