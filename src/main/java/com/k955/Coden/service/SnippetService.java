package com.k955.Coden.service;

import com.k955.Coden.dtos.Snippet.*;
import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SnippetService {

    SnippetResponse createSnippet(@Valid SnippetRequest snippetRequest);

    SnippetResponse getSnippetById(UUID snippetId);

    SnippetResponse updateSnippetById(UUID snippetId, UpdateSnippetRequest updateSnippetRequest);

    void deleteSnippet(UUID snippetId);

    SnippetResponse updateSnippetStatus(UUID snippetId, UpdateSnippetStatus updateSnippetStatus);

    Page<SnippetView> getSnippets(Language language, Framework framework, SnippetType snippetType, SnippetStatus snippetStatus, String search, Pageable pageable);

}
