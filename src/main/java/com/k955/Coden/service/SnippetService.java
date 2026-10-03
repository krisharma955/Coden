package com.k955.Coden.service;

import com.k955.Coden.dtos.Snippet.SnippetRequest;
import com.k955.Coden.dtos.Snippet.SnippetResponse;
import com.k955.Coden.dtos.Snippet.UpdateSnippetRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface SnippetService {

    SnippetResponse createSnippet(@Valid SnippetRequest snippetRequest);

    SnippetResponse getSnippetById(UUID snippetId);

    SnippetResponse updateSnippetById(UUID snippetId, UpdateSnippetRequest updateSnippetRequest);

    void deleteSnippet(UUID snippetId);

}
