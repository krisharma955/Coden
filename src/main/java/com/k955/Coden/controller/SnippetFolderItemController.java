package com.k955.Coden.controller;

import com.k955.Coden.dtos.SnippetFolderItem.SnippetFolderItemResponse;
import com.k955.Coden.service.SnippetFolderItemService;
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
@RequestMapping("/api")
public class SnippetFolderItemController {

    private final SnippetFolderItemService snippetFolderItemService;

    @PostMapping("/folders/{folderId}/snippets/{snippetId}")
    public ResponseEntity<SnippetFolderItemResponse> addSnippetToFolder(
            @PathVariable UUID folderId, @PathVariable UUID snippetId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(snippetFolderItemService.addSnippetToFolder(folderId, snippetId));
    }

    @GetMapping("/folders/{folderId}/snippets")
    public ResponseEntity<Page<SnippetFolderItemResponse>> getSnippetsFromFolder(
            @PathVariable UUID folderId,
            @PageableDefault(size = 5, sort = "addedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(snippetFolderItemService.getSnippetsFromFolder(folderId, pageable));
    }

    @DeleteMapping("/folders/{folderId}/snippets/{snippetId}")
    public ResponseEntity<Void> removeSnippetFromFolder(
            @PathVariable UUID folderId, @PathVariable UUID snippetId
    ) {
        snippetFolderItemService.removeSnippetFromFolder(folderId, snippetId);
        return ResponseEntity.noContent().build();
    }

}
