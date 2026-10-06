package com.k955.Coden.controller;

import com.k955.Coden.dtos.Bundle.BundleRequest;
import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.dtos.Bundle.UpdateBundleRequest;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.service.BundleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bundles")
public class BundleController {

    private final BundleService bundleService;

    @PostMapping
    public ResponseEntity<BundleResponse> createBundle(@Valid @RequestBody BundleRequest bundleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bundleService.createBundle(bundleRequest));
    }

    @GetMapping("/{bundleId}")
    public ResponseEntity<BundleResponse> getBundleById(@PathVariable UUID bundleId) {
        return ResponseEntity.ok(bundleService.getBundleById(bundleId));
    }

    @GetMapping
    public ResponseEntity<Page<BundleResponse>> getBundles(
            @RequestParam(required = false) Language language,
            @RequestParam(required = false) BundleCategory bundleCategory,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 5, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(bundleService.getBundles(language, bundleCategory, search, pageable));
    }

    @PatchMapping("/{bundleId}")
    public ResponseEntity<BundleResponse> updateBundleById(
            @PathVariable UUID bundleId, @RequestBody UpdateBundleRequest updateBundleRequest
    ) {
        return ResponseEntity.ok(bundleService.updateBundleById(bundleId, updateBundleRequest));
    }

    @DeleteMapping("/{bundleId}")
    public ResponseEntity<Void> deleteBundle(@PathVariable UUID bundleId) {
        bundleService.deleteBundle(bundleId);
        return ResponseEntity.noContent().build();
    }


}
