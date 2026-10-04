package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Folder.FolderRequest;
import com.k955.Coden.dtos.Folder.FolderResponse;
import com.k955.Coden.dtos.Folder.UpdateFolderRequest;
import com.k955.Coden.entity.Folder;
import com.k955.Coden.entity.User;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.FolderMapper;
import com.k955.Coden.repository.FolderRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.FolderService;
import com.k955.Coden.specification.FolderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;

    @Override
    @Transactional
    public FolderResponse createFolder(FolderRequest folderRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        boolean check = folderRepository.existsByName(folderRequest.name());
        if(check) throw new BadRequestException("A folder with name: " + folderRequest.name() + " already exists");

        Folder folder = Folder.builder()
                .user(user)
                .name(folderRequest.name())
                .description(folderRequest.description())
                .build();
        Folder saved = folderRepository.save(folder);

        return folderMapper.toFolderResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FolderResponse getFolderById(UUID folderId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        if(!folder.getUser().getId().equals(userId)) throw new AccessDeniedException("You can only access your folders");

        return folderMapper.toFolderResponse(folder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FolderResponse> getFolders(String search, Pageable pageable) {
        return folderRepository
                .findAll(FolderSpecification.filterBy(search), pageable)
                .map(folderMapper::toFolderResponse);
    }

    @Override
    @Transactional
    public FolderResponse updateFolder(UUID folderId, UpdateFolderRequest updateFolderRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        if(!folder.getUser().getId().equals(userId)) throw new AccessDeniedException("You can only access your folders");

        if(updateFolderRequest.name() != null && !updateFolderRequest.name().isBlank()) {
            folder.setName(updateFolderRequest.name());
        }

        if(updateFolderRequest.description() != null && !updateFolderRequest.description().isBlank()) {
            folder.setDescription(updateFolderRequest.description());
        }

        Folder saved = folderRepository.save(folder);

        return folderMapper.toFolderResponse(saved);
    }

    @Override
    @Transactional
    public void deleteFolder(UUID folderId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException(folderId.toString(), "Folder"));

        if(!folder.getUser().getId().equals(userId)) throw new AccessDeniedException("You can only access your folders");

        folderRepository.delete(folder);
    }

}
