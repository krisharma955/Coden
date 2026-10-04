package com.k955.Coden.controller;

import com.k955.Coden.dtos.Folder.FolderRequest;
import com.k955.Coden.dtos.Folder.FolderResponse;
import com.k955.Coden.dtos.Folder.UpdateFolderRequest;
import com.k955.Coden.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(@Valid @RequestBody FolderRequest folderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(folderRequest));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponse> getFolderById(@PathVariable UUID folderId) {
        return ResponseEntity.ok(folderService.getFolderById(folderId));
    }

    @GetMapping
    public ResponseEntity<Page<FolderResponse>> getFolders(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(folderService.getFolders(search, pageable));
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponse> updateFolder(
            @PathVariable UUID folderId, @RequestBody UpdateFolderRequest updateFolderRequest
    ) {
        return ResponseEntity.ok(folderService.updateFolder(folderId, updateFolderRequest));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable UUID folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

}
