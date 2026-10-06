package com.k955.Coden.service;

import com.k955.Coden.dtos.BundleFile.BundleFileResponse;
import com.k955.Coden.dtos.BundleFile.UpdateBundleFileRequest;
import com.k955.Coden.enums.Common.Language;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BundleFileService {

    BundleFileResponse getBundleFileById(UUID bundleId, UUID fileId);

    List<BundleFileResponse> getBundleFile(UUID bundleId);

    BundleFileResponse updateBundleFile(UUID bundleId, UUID fileId, UpdateBundleFileRequest updateBundleFileRequest);

    void deleteBundleFile(UUID bundleId, UUID fileId);

    BundleFileResponse createBundleFile(UUID bundleId, MultipartFile file, String filePath, Language language);

}
