package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.SnippetStar.SnippetStarResponse;
import com.k955.Coden.entity.Snippet;
import com.k955.Coden.entity.SnippetStar;
import com.k955.Coden.entity.User;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.SnippetStarMapper;
import com.k955.Coden.repository.SnippetRepository;
import com.k955.Coden.repository.SnippetStarRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.SnippetStarService;
import com.k955.Coden.specification.SnippetStarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnippetStarServiceImpl implements SnippetStarService {

    private final SnippetStarRepository snippetStarRepository;
    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;
    private final SnippetStarMapper snippetStarMapper;

    @Override
    @Transactional
    public SnippetStarResponse starSnippet(UUID snippetId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));

        boolean check = snippetStarRepository.existsBySnippetIdAndUserId(snippetId, userId);
        if(check) throw new BadRequestException("Already Starred");

        SnippetStar snippetStar = SnippetStar.builder()
                .user(user)
                .snippet(snippet)
                .build();
        SnippetStar saved = snippetStarRepository.save(snippetStar);

        return snippetStarMapper.toSnippetStarResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnippetStarResponse> getStarredSnippets(String search, Pageable pageable) {
        return snippetStarRepository
                .findAll(SnippetStarSpecification.filterBy(search), pageable)
                .map(snippetStarMapper::toSnippetStarResponse);
    }

    @Override
    @Transactional
    public void unstarSnippet(UUID snippetId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        SnippetStar snippetStar = snippetStarRepository.findBySnippetIdAndUserId(snippetId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "SnippetStar"));

        snippetStarRepository.delete(snippetStar);
    }

}
