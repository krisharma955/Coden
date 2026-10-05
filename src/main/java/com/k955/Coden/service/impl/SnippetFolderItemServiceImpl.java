package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.SnippetFolderItem.SnippetFolderItemResponse;
import com.k955.Coden.entity.Folder;
import com.k955.Coden.entity.Snippet;
import com.k955.Coden.entity.SnippetFolderItem;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.SnippetFolderItemMapper;
import com.k955.Coden.repository.FolderRepository;
import com.k955.Coden.repository.SnippetFolderItemRepository;
import com.k955.Coden.repository.SnippetRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.SnippetFolderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnippetFolderItemServiceImpl implements SnippetFolderItemService {

    private final SnippetFolderItemRepository snippetFolderItemRepository;
    private final SnippetRepository snippetRepository;
    private final FolderRepository folderRepository;
    private final SnippetFolderItemMapper snippetFolderItemMapper;
    private final JwtAuthUtil jwtAuthUtil;

    @Override
    @Transactional
    public SnippetFolderItemResponse addSnippetToFolder(UUID folderId, UUID snippetId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new ResourceNotFoundException(snippetId.toString(), "Snippet"));

        if(!folder.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your folders");
        }

        boolean check = snippetFolderItemRepository.existsByFolderIdAndSnippetId(folderId, snippetId);
        if(check) throw new BadRequestException("Snippet is already present in this folder");

        SnippetFolderItem snippetFolderItem = SnippetFolderItem.builder()
                .folder(folder)
                .snippet(snippet)
                .build();
        SnippetFolderItem saved = snippetFolderItemRepository.save(snippetFolderItem);

        return snippetFolderItemMapper.toSnippetFolderItemResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnippetFolderItemResponse> getSnippetsFromFolder(UUID folderId, Pageable pageable) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        if(!folder.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your folders");
        }

        return snippetFolderItemRepository
                .findByFolderId(folderId, pageable)
                .map(snippetFolderItemMapper::toSnippetFolderItemResponse);
    }

    @Override
    @Transactional
    public void removeSnippetFromFolder(UUID folderId, UUID snippetId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        if(!folder.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your folders");
        }

        boolean check = snippetFolderItemRepository.existsByFolderIdAndSnippetId(folderId, snippetId);
        if(!check) throw new ResourceNotFoundException(snippetId.toString(), "Snippet in folder");

        snippetFolderItemRepository.deleteByFolderIdAndSnippetId(folderId, snippetId);
    }

}
