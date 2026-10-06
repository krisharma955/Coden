package com.k955.Coden.service;

import com.k955.Coden.dtos.Bundle.BundleRequest;
import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.dtos.Bundle.UpdateBundleRequest;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Common.Language;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BundleService {

    BundleResponse createBundle(@Valid BundleRequest bundleRequest);

    BundleResponse getBundleById(UUID bundleId);

    Page<BundleResponse> getBundles(Language language, BundleCategory bundleCategory, String search, Pageable pageable);

    BundleResponse updateBundleById(UUID bundleId, UpdateBundleRequest updateBundleRequest);

    void deleteBundle(UUID bundleId);

}
