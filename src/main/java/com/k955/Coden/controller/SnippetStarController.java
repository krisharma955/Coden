package com.k955.Coden.controller;

import com.k955.Coden.dtos.SnippetStar.SnippetStarResponse;
import com.k955.Coden.service.SnippetStarService;
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
@RequestMapping("/api/stars")
public class SnippetStarController {

    private final SnippetStarService snippetStarService;

    @PostMapping("/{snippetId}")
    public ResponseEntity<SnippetStarResponse> starSnippet(@PathVariable UUID snippetId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(snippetStarService.starSnippet(snippetId));
    }

    @GetMapping
    public ResponseEntity<Page<SnippetStarResponse>> getStarredSnippets(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 5, sort = "starredAt" ,direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(snippetStarService.getStarredSnippets(search, pageable));
    }

    @DeleteMapping("/{snippetId}")
    public ResponseEntity<Void> unstarSnippet(@PathVariable UUID snippetId) {
        snippetStarService.unstarSnippet(snippetId);
        return ResponseEntity.noContent().build();
    }

}
