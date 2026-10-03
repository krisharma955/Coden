package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Snippet.SnippetRequest;
import com.k955.Coden.dtos.Snippet.SnippetResponse;
import com.k955.Coden.dtos.Snippet.UpdateSnippetRequest;
import com.k955.Coden.mapper.SnippetMapper;
import com.k955.Coden.repository.SnippetRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.service.SnippetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnippetServiceImpl implements SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private final SnippetMapper snippetMapper;

    @Override
    public SnippetResponse createSnippet(SnippetRequest snippetRequest) {
        return null;
    }

    @Override
    public SnippetResponse getSnippetById(UUID snippetId) {
        return null;
    }

    @Override
    public SnippetResponse updateSnippetById(UUID snippetId, UpdateSnippetRequest updateSnippetRequest) {
        return null;
    }

    @Override
    public void deleteSnippet(UUID snippetId) {

    }

}
