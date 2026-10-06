package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Bundle.BundleRequest;
import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.dtos.Bundle.UpdateBundleRequest;
import com.k955.Coden.entity.Bundle;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.BundleMapper;
import com.k955.Coden.repository.BundleRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.BundleService;
import com.k955.Coden.specification.BundleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BundleServiceImpl implements BundleService {

    private final BundleRepository bundleRepository;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;
    private final BundleMapper bundleMapper;

    @Override
    @Transactional
    public BundleResponse createBundle(BundleRequest bundleRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new BadRequestException("Only Admins can create Bundles");
        }

        Bundle bundle = Bundle.builder()
                .name(bundleRequest.name())
                .description(bundleRequest.description())
                .bundleStatus(bundleRequest.bundleStatus())
                .bundleCategory(bundleRequest.bundleCategory())
                .createdBy(user)
                .build();
        Bundle saved = bundleRepository.save(bundle);

        return bundleMapper.toBundleResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BundleResponse getBundleById(UUID bundleId) {
        getVisibleBundle(bundleId);
        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));
        return bundleMapper.toBundleResponse(bundle);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleResponse> getBundles(Language language, BundleCategory bundleCategory, String search, Pageable pageable) {
        return bundleRepository
                .findAll(BundleSpecification.filterBy(language, bundleCategory, search), pageable)
                .map(bundleMapper::toBundleResponse);
    }

    @Override
    @Transactional
    public BundleResponse updateBundleById(UUID bundleId, UpdateBundleRequest updateBundleRequest) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new BadRequestException("Only Admins can update Bundles");
        }

        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        if(!bundle.getCreatedBy().getId().equals(userId) && !user.getRole().equals(Role.SUPER_ADMIN)) {
            throw new AccessDeniedException("Only the creator/super-admin can update a Bundle");
        }

        if(updateBundleRequest.name() != null) {
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

        return bundleMapper.toBundleResponse(saved);
    }

    // TODO: once MinIO is wired in, delete the objects for bundle.getFiles()
    @Override
    @Transactional
    public void deleteBundle(UUID bundleId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        if(!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))) {
            throw new BadRequestException("Only Admins can delete Bundles");
        }

        Bundle bundle = bundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException(bundleId.toString(), "Bundle"));

        if(!bundle.getCreatedBy().getId().equals(userId) && !user.getRole().equals(Role.SUPER_ADMIN)) {
            throw new AccessDeniedException("Only the creator/super-admin can delete a Bundle");
        }

        bundleRepository.delete(bundle);
    }

    /// Utiltiy Methods

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
