package com.k955.Coden.controller;

import com.k955.Coden.dtos.Snippet.SnippetRequest;
import com.k955.Coden.dtos.Snippet.SnippetResponse;
import com.k955.Coden.dtos.Snippet.UpdateSnippetRequest;
import com.k955.Coden.service.SnippetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/{snippetId}")
    public ResponseEntity<SnippetResponse> updateSnippetById(
            @PathVariable UUID snippetId, @RequestBody UpdateSnippetRequest updateSnippetRequest
    ) {
       return ResponseEntity.ok(snippetService.updateSnippetById(snippetId, updateSnippetRequest));
    }

    @DeleteMapping("/{snippetId}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable UUID snippetId) {
        snippetService.deleteSnippet(snippetId);
        return ResponseEntity.noContent().build();
    }

}
