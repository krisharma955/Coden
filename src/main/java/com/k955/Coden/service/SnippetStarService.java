package com.k955.Coden.service;

import com.k955.Coden.dtos.SnippetStar.SnippetStarResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SnippetStarService {

    SnippetStarResponse starSnippet(UUID snippetId);

    Page<SnippetStarResponse> getStarredSnippets(String search, Pageable pageable);

    void unstarSnippet(UUID snippetId);

}
