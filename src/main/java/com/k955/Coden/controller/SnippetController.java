package com.k955.Coden.controller;

import com.k955.Coden.dtos.Snippet.*;
import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;
import com.k955.Coden.service.SnippetService;
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
@RequestMapping("/api/snippets")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    public ResponseEntity<SnippetResponse> createSnippet(@Valid @RequestBody SnippetRequest snippetRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(snippetService.createSnippet(snippetRequest));
    }

    @GetMapping("/{snippetId}")
    public ResponseEntity<SnippetResponse> getSnippetById(@PathVariable UUID snippetId) {
        return ResponseEntity.ok(snippetService.getSnippetById(snippetId));
    }

    @GetMapping
    public ResponseEntity<Page<SnippetView>> getSnippets(
            @RequestParam(required = false) Language language,
            @RequestParam(required = false) Framework framework,
            @RequestParam(required = false) SnippetType snippetType,
            @RequestParam(required = false) SnippetStatus snippetStatus,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 5, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(snippetService.getSnippets(language, framework, snippetType, snippetStatus, search, pageable));
    }

    @PatchMapping("/{snippetId}")
    public ResponseEntity<SnippetResponse> updateSnippetById(
            @PathVariable UUID snippetId, @RequestBody UpdateSnippetRequest updateSnippetRequest
    ) {
       return ResponseEntity.ok(snippetService.updateSnippetById(snippetId, updateSnippetRequest));
    }

    @PatchMapping("/review/{snippetId}")
    public ResponseEntity<SnippetResponse> reviewSnippet(
            @PathVariable UUID snippetId, @RequestBody UpdateSnippetStatus updateSnippetStatus
    ) {
        return ResponseEntity.ok(snippetService.updateSnippetStatus(snippetId, updateSnippetStatus));
    }

    @DeleteMapping("/{snippetId}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable UUID snippetId) {
        snippetService.deleteSnippet(snippetId);
        return ResponseEntity.noContent().build();
    }

}
