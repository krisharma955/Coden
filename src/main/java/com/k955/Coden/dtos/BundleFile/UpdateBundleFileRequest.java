package com.k955.Coden.dtos.BundleFile;

import com.k955.Coden.enums.Common.Language;

public record UpdateBundleFileRequest(
        String fileName,
        String filePath,
        Language language,
        String extension
) {
}
