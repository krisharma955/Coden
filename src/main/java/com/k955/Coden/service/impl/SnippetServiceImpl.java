package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Snippet.*;
import com.k955.Coden.entity.Snippet;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Notification.NotificationType;
import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.SnippetMapper;
import com.k955.Coden.repository.SnippetRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.NotificationService;
import com.k955.Coden.service.SnippetService;
import com.k955.Coden.specification.SnippetSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnippetServiceImpl implements SnippetService {

    private final NotificationService notificationService;
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

        boolean isSuperAdmin = user.getRole().equals(Role.SUPER_ADMIN);

        if(!isSuperAdmin) {
            if (user.getRole().equals(Role.USER)) {
                notificationService.notifyAdmins(
                        NotificationType.SNIPPET_CREATED,
                        snippet.getId(), "Snippet Created", userId);
            } else if (user.getRole().equals(Role.ADMIN)) {
                notificationService.notifySuperAdmins(
                        NotificationType.SNIPPET_CREATED,
                        snippet.getId(), "Snippet Created", userId);
            }
        }

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
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new AccessDeniedException("Only Admins can Delete Snippets");
        }

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));
        snippet.setDeletedAt(Instant.now());

        snippetRepository.delete(snippet);
    }

    @Override
    @Transactional
    public SnippetResponse updateSnippetStatus(UUID snippetId, UpdateSnippetStatus updateSnippetStatus) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new AccessDeniedException("Only Admins can Review Snippets");
        }

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));

        boolean duplicateChange = snippet.getSnippetStatus().equals(updateSnippetStatus.snippetStatus());

        if(!duplicateChange) {
            snippet.setSnippetStatus(updateSnippetStatus.snippetStatus());
            snippet.setReviewedBy(user);

            Snippet saved = snippetRepository.save(snippet);

            if (snippet.getSnippetStatus().equals(SnippetStatus.APPROVED) || snippet.getSnippetStatus().equals(SnippetStatus.REJECTED)) {
                NotificationType notificationType =
                        updateSnippetStatus.snippetStatus().equals(SnippetStatus.APPROVED)
                                ? NotificationType.SNIPPET_APPROVED : NotificationType.SNIPPET_REJECTED;

                boolean isSelfReview = snippet.getCreatedBy().getId().equals(user.getId());

                if (!isSelfReview) {
                    notificationService.notifyUser(notificationType, snippetId, "Snippet Status Updated", userId, snippet.getCreatedBy().getId());
                }

                notificationService.notifySuperAdmins(notificationType, snippetId, "Snippet Status Updated", userId);
            }

            return snippetMapper.toSnippetResponse(saved);
        }

        return snippetMapper.toSnippetResponse(snippet);
    }

    @Override
    public Page<SnippetView> getSnippets(
            Language language, Framework framework, SnippetType snippetType,
            SnippetStatus snippetStatus, String search, Pageable pageable
    ) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

       boolean isAdmin = user.getRole().equals(Role.SUPER_ADMIN) || user.getRole().equals(Role.ADMIN);

       SnippetStatus effectiveStatus = isAdmin ? snippetStatus : SnippetStatus.APPROVED;

        return snippetRepository.findAll(
                SnippetSpecification.filterBy(
                        language,
                        framework,
                        snippetType,
                        effectiveStatus,
                        search
                ),
                pageable
        ).map(snippetMapper::toSnippetView);
    }

}
