package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Snippet.SnippetRequest;
import com.k955.Coden.dtos.Snippet.SnippetResponse;
import com.k955.Coden.dtos.Snippet.UpdateSnippetRequest;
import com.k955.Coden.entity.Snippet;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.SnippetMapper;
import com.k955.Coden.repository.SnippetRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.SnippetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnippetServiceImpl implements SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private final SnippetMapper snippetMapper;
    private final JwtAuthUtil jwtAuthUtil;

    @Override
    @Transactional
    public SnippetResponse createSnippet(SnippetRequest snippetRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        Snippet snippet = Snippet.builder()
                .title(snippetRequest.title())
                .description(snippetRequest.description())
                .code(snippetRequest.code())
                .language(snippetRequest.language())
                .framework(snippetRequest.framework())
                .snippetType(snippetRequest.snippetType())
                .createdBy(user)
                .build();
        Snippet saved = snippetRepository.save(snippet);

        return snippetMapper.toSnippetResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SnippetResponse getSnippetById(UUID snippetId) {
        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));
        return snippetMapper.toSnippetResponse(snippet);
    }

    @Override
    @Transactional
    public SnippetResponse updateSnippetById(UUID snippetId, UpdateSnippetRequest updateSnippetRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN) || snippet.getCreatedBy().getId().equals(userId))) {
            throw new AccessDeniedException("Only Admins or Creators can edit Snippets");
        }

        if(updateSnippetRequest.title() != null) {
            snippet.setTitle(updateSnippetRequest.title());
        }

        if(updateSnippetRequest.description() != null) {
            snippet.setDescription(updateSnippetRequest.description());
        }

        if(updateSnippetRequest.code() != null) {
            snippet.setCode(updateSnippetRequest.code());
        }

        if(updateSnippetRequest.language() != null) {
            snippet.setLanguage(updateSnippetRequest.language());
        }

        if(updateSnippetRequest.framework() != null) {
            snippet.setFramework(updateSnippetRequest.framework());
        }

        if(updateSnippetRequest.snippetType() != null) {
            snippet.setSnippetType(updateSnippetRequest.snippetType());
        }

        Snippet saved = snippetRepository.save(snippet);

        return snippetMapper.toSnippetResponse(saved);
    }

    @Override
    @Transactional
    public void deleteSnippet(UUID snippetId) {
        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));
        snippet.setDeletedAt(Instant.now());
        snippetRepository.delete(snippet);
    }

}
