package com.k955.Coden.controller;

import com.k955.Coden.dtos.BundleFile.BundleFileResponse;
import com.k955.Coden.dtos.BundleFile.UpdateBundleFileRequest;
import com.k955.Coden.enums.Common.Language;
import com.k955.Coden.service.BundleFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bundles")
public class BundleFileController {

    private final BundleFileService bundleFileService;

    @PostMapping(value = "/{bundleId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BundleFileResponse> createBundleFile(
            @PathVariable UUID bundleId,
            @RequestPart("file") MultipartFile file,
            @RequestParam String filePath,
            @RequestParam Language language
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bundleFileService.createBundleFile(bundleId, file, filePath, language));
    }

    @GetMapping("/{bundleId}/files/{fileId}")
    public ResponseEntity<BundleFileResponse> getBundleFileById(
            @PathVariable UUID bundleId, @PathVariable UUID fileId
    ) {
        return ResponseEntity.ok(bundleFileService.getBundleFileById(bundleId, fileId));
    }

    @GetMapping("/{bundleId}/files")
    public ResponseEntity<List<BundleFileResponse>> getBundleFiles(@PathVariable UUID bundleId) {
        return ResponseEntity.ok(bundleFileService.getBundleFile(bundleId));
    }

    @PatchMapping("/{bundleId}/files/{fileId}")
    public ResponseEntity<BundleFileResponse> updateBundleFile(
            @PathVariable UUID bundleId, @PathVariable UUID fileId,
            @RequestBody UpdateBundleFileRequest updateBundleFileRequest
    ) {
        return ResponseEntity.ok(bundleFileService.updateBundleFile(bundleId, fileId, updateBundleFileRequest));
    }

    @DeleteMapping("/{bundleId}/files/{fileId}")
    public ResponseEntity<Void> deleteBundleFile(
            @PathVariable UUID bundleId, @PathVariable UUID fileId
    ) {
        bundleFileService.deleteBundleFile(bundleId, fileId);
        return ResponseEntity.noContent().build();
    }

}
