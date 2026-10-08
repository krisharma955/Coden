package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.BundleFile.BundleFileResponse;
import com.k955.Coden.dtos.BundleFile.UpdateBundleFileRequest;
import com.k955.Coden.entity.Bundle;
import com.k955.Coden.entity.BundleFile;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Bundle.BundleStatus;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.enums.Notification.NotificationType;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.BundleFileMapper;
import com.k955.Coden.repository.BundleFileRepository;
import com.k955.Coden.repository.BundleRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.BundleFileService;
import com.k955.Coden.service.NotificationService;
import com.k955.Coden.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BundleFileServiceImpl implements BundleFileService {

    private final NotificationService notificationService;
    private final BundleFileRepository bundleFileRepository;
    private final BundleRepository bundleRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final JwtAuthUtil jwtAuthUtil;
    private final BundleFileMapper bundleFileMapper;

    @Override
    @Transactional
    public BundleFileResponse createBundleFile(UUID bundleId, MultipartFile file, String filePath, Language language) {
        User user = getCurrentAdmin("upload");

        if(file.isEmpty()) {
            throw new BadRequestException("File is Empty");
        }

        String cleanPath = validatePath(filePath);

        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        if(bundleFileRepository.existsByBundleIdAndFilePath(bundleId, cleanPath)) {
            throw new BadRequestException("A file with this path already exists in the bundle");
        }

        String fileName = extractFileName(cleanPath);

        String objectKey = "bundles/" + bundleId + "/" + UUID.randomUUID() + "-" + fileName;

        storageService.upload(objectKey, file);

        try {
            BundleFile saved = bundleFileRepository.saveAndFlush(BundleFile.builder()
                    .bundle(bundle)
                    .fileName(fileName)
                    .filePath(cleanPath)
                    .extension(extractExtension(fileName))
                    .size(file.getSize())
                    .objectKey(objectKey)
                    .language(language)
                    .uploadedBy(user)
                    .build());

            notificationService.notifySuperAdmins(
                    NotificationType.BUNDLE_FILE_CREATED,
                    saved.getId(),
                    "Bundle File Created",
                    user.getId());

            return bundleFileMapper.toBundleFileResponse(saved);
        } catch (RuntimeException e) {
            storageService.delete(objectKey);   // don't leave an orphan in MinIO
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BundleFileResponse getBundleFileById(UUID bundleId, UUID fileId) {
        getVisibleBundle(bundleId);
        BundleFile bundleFile = bundleFileRepository.findByIdAndBundleId(fileId, bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(fileId.toString(), "BundleFile"));
        return bundleFileMapper.toBundleFileResponse(bundleFile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BundleFileResponse> getBundleFile(UUID bundleId) {
        getVisibleBundle(bundleId);
        return bundleFileRepository.findByBundleId(bundleId).stream()
                .map(bundleFileMapper::toBundleFileResponse)
                .toList();
    }

    @Override
    @Transactional
    public BundleFileResponse updateBundleFile(UUID bundleId, UUID fileId, UpdateBundleFileRequest updateBundleFileRequest) {
        User user = getCurrentAdmin("update");

        BundleFile bundleFile = bundleFileRepository.findByIdAndBundleId(fileId, bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(fileId.toString(), "BundleFile"));

        checkCanModify(bundleFile, user);

        if (updateBundleFileRequest.filePath() != null) {
            String cleanPath = validatePath(updateBundleFileRequest.filePath());
            if (!cleanPath.equals(bundleFile.getFilePath())
                    && bundleFileRepository.existsByBundleIdAndFilePath(bundleId, cleanPath)) {
                throw new BadRequestException("A file with this path already exists in the bundle");
            }

            String fileName = extractFileName(cleanPath);
            bundleFile.setFilePath(cleanPath);
            bundleFile.setFileName(fileName);
            bundleFile.setExtension(extractExtension(fileName));
        }

        if (updateBundleFileRequest.fileName() != null) {
            String name = updateBundleFileRequest.fileName().trim();
            if (name.isEmpty() || name.contains("/") || name.contains("\\") || name.contains("..")) {
                throw new BadRequestException("Invalid file name");
            }
            if (name.length() > 150) {
                throw new BadRequestException("File name is too long");
            }
            bundleFile.setFileName(name);
            bundleFile.setExtension(extractExtension(name));
        }

        if (updateBundleFileRequest.extension() != null) {
            String extension = updateBundleFileRequest.extension().trim().toLowerCase();
            if (!extension.isEmpty() && !extension.startsWith(".")) {
                extension = "." + extension;
            }
            if (extension.length() > 20) {
                throw new BadRequestException("Invalid extension");
            }
            bundleFile.setExtension(extension);
        }

        if(updateBundleFileRequest.language() != null) {
            bundleFile.setLanguage(updateBundleFileRequest.language());
        }

        BundleFile saved = bundleFileRepository.save(bundleFile);

        notificationService.notifySuperAdmins(
                NotificationType.BUNDLE_FILE_UPDATED,
                saved.getId(),
                "Bundle File Created",
                user.getId());

        return bundleFileMapper.toBundleFileResponse(saved);
    }

    @Override
    @Transactional
    public void deleteBundleFile(UUID bundleId, UUID fileId) {
        User user = getCurrentAdmin("delete");
        BundleFile bundleFile = bundleFileRepository.findByIdAndBundleId(fileId, bundleId)
                        .orElseThrow(() -> new ResourceNotFoundException(fileId.toString(), "BundleFile"));
        checkCanModify(bundleFile, user);

        String objectKey = bundleFile.getObjectKey();
        bundleFileRepository.delete(bundleFile);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                storageService.delete(objectKey);
            }
        });
    }

    /// Utility Methods

    private User getCurrentAdmin(String action) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Only Admins can " + action + " files");
        }
        return user;
    }

    private String validatePath(String filePath) {
        String cleanPath = filePath.trim();
        if (cleanPath.isEmpty() || cleanPath.startsWith("/") || cleanPath.contains("..") || cleanPath.contains("\\")) {
            throw new BadRequestException("Invalid file path");
        }
        if (cleanPath.length() > 1000) {
            throw new BadRequestException("Invalid file path");
        }
        if (extractFileName(cleanPath).length() > 150) {   // object_key VARCHAR(255) = 82 + fileName
            throw new BadRequestException("File name is too long");
        }
        if (extractExtension(extractFileName(cleanPath)).length() > 20) {
            throw new BadRequestException("Invalid extension");
        }
        return cleanPath;
    }

    private String extractFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private String extractExtension(String fileName) {
        return fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
                : "";
    }

    private void checkCanModify(BundleFile file, User user) {
        boolean isUploader = file.getUploadedBy().getId().equals(user.getId());
        if (!isUploader && user.getRole() != Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Only the uploader/super-admin can modify this file");
        }
    }

    private void getVisibleBundle(UUID bundleId) {
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        // non-admins only see PUBLISHED bundles; to them a draft simply doesn't exist
        if (bundle.getBundleStatus() != BundleStatus.PUBLISHED && !isAdminCaller()) {
            throw new ResourceNotFoundException(bundleId.toString(), "Bundle");
        }
    }

    private boolean isAdminCaller() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ROLE_SUPER_ADMIN"));
    }

}
