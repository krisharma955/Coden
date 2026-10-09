package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Bundle.BundleRequest;
import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.dtos.Bundle.UpdateBundleRequest;
import com.k955.Coden.entity.Bundle;
import com.k955.Coden.entity.BundleFile;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;
import com.k955.Coden.enums.Notification.NotificationType;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.DataIntegrityViolationException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.exception.StorageException;
import com.k955.Coden.mapper.BundleMapper;
import com.k955.Coden.repository.BundleFileRepository;
import com.k955.Coden.repository.BundleRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.BundleService;
import com.k955.Coden.service.NotificationService;
import com.k955.Coden.service.StorageService;
import com.k955.Coden.specification.BundleSpecification;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BundleServiceImpl implements BundleService {

    private final NotificationService notificationService;
    private final BundleRepository bundleRepository;
    private final BundleFileRepository bundleFileRepository;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;
    private final BundleMapper bundleMapper;
    private final StorageService storageService;

    @Override
    @Transactional
    public BundleResponse createBundle(BundleRequest bundleRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new AccessDeniedException("Only Admins can create Bundles");
        }

        boolean check = bundleRepository.existsByName(bundleRequest.name());
        if(check) throw new DataIntegrityViolationException("A bundle with name " + bundleRequest.name() + " already exists");

        Bundle bundle = Bundle.builder()
                .name(bundleRequest.name())
                .description(bundleRequest.description())
                .bundleStatus(bundleRequest.bundleStatus())
                .bundleCategory(bundleRequest.bundleCategory())
                .createdBy(user)
                .build();
        Bundle saved = bundleRepository.save(bundle);

        notificationService.notifySuperAdmins(
                NotificationType.BUNDLE_CREATED,
                saved.getId(),
                "Bundle Created",
                user.getId());

        return bundleMapper.toBundleResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BundleResponse getBundleById(UUID bundleId) {
        Bundle bundle = getVisibleBundle(bundleId);
        return bundleMapper.toBundleResponse(bundle);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleResponse> getBundles(BundleCategory bundleCategory, String search, Pageable pageable) {
        return bundleRepository
                .findAll(BundleSpecification.filterBy(bundleCategory, search, isAdminCaller()), pageable)
                .map(bundleMapper::toBundleResponse);
    }

    @Override
    @Transactional
    public BundleResponse updateBundleById(UUID bundleId, UpdateBundleRequest updateBundleRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new AccessDeniedException("Only Admins can update Bundles");
        }

        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        if(!bundle.getCreatedBy().getId().equals(userId) && !user.getRole().equals(Role.SUPER_ADMIN)) {
            throw new AccessDeniedException("Only the creator/super-admin can update a Bundle");
        }

        if(updateBundleRequest.name() != null && !updateBundleRequest.name().equals(bundle.getName())) {
            if(bundleRepository.existsByName(updateBundleRequest.name())) {
                throw new DataIntegrityViolationException("A bundle with name " + updateBundleRequest.name() + " already exists");
            }
            bundle.setName(updateBundleRequest.name());
        }

        if(updateBundleRequest.description() != null) {
            bundle.setDescription(updateBundleRequest.description());
        }

        if(updateBundleRequest.bundleCategory() != null) {
            bundle.setBundleCategory(updateBundleRequest.bundleCategory());
        }

        if(updateBundleRequest.bundleStatus() != null) {
            bundle.setBundleStatus(updateBundleRequest.bundleStatus());
        }

        Bundle saved = bundleRepository.save(bundle);

        notificationService.notifySuperAdmins(
                NotificationType.BUNDLE_UPDATED,
                saved.getId(),
                "Bundle Updated",
                user.getId());

        return bundleMapper.toBundleResponse(saved);
    }

    @Override
    @Transactional
    public void deleteBundle(UUID bundleId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new AccessDeniedException("Only Admins can delete Bundles");
        }

        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        if(!bundle.getCreatedBy().getId().equals(userId) && !user.getRole().equals(Role.SUPER_ADMIN)) {
            throw new AccessDeniedException("Only the creator/super-admin can delete a Bundle");
        }

        List<String> objectKeys = bundleFileRepository.findByBundleId(bundleId).stream()
                .map(BundleFile::getObjectKey)
                .toList();

        notificationService.notifySuperAdmins(
                NotificationType.BUNDLE_DELETED,
                bundle.getId(),
                "Bundle Deleted",
                user.getId());

        bundleRepository.delete(bundle);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                objectKeys.forEach(storageService::delete);
            }
        });
    }

    @Override
    public void downloadBundleAsZip(UUID bundleId, HttpServletResponse response) {
        Bundle bundle = getVisibleBundle(bundleId);

        List<BundleFile> files = bundleFileRepository.findByBundleId(bundleId).stream()
                .sorted(Comparator.comparing(BundleFile::getFilePath))
                .toList();

        if (files.isEmpty()) {
            throw new BadRequestException("Bundle has no files");
        }

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition
                .attachment()
                .filename(bundle.getName() + ".zip")
                .build()
                .toString());

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for (BundleFile file : files) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getFilePath()));
                try (InputStream in = storageService.download(file.getObjectKey())) {
                    in.transferTo(zipOutputStream);
                }
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            log.error("Failed to build zip for bundle {}", bundleId, e);
            throw new StorageException("Failed to build download zip", e);
        }
    }

    /// Utiltiy Methods

    private Bundle getVisibleBundle(UUID bundleId) {
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        // non-admins only see PUBLISHED bundles; to them a draft simply doesn't exist
        if (bundle.getBundleStatus() != BundleStatus.PUBLISHED && !isAdminCaller()) {
            throw new ResourceNotFoundException(bundleId.toString(), "Bundle");
        }
        return bundle;
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
