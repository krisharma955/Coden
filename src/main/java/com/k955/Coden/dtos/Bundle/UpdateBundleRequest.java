package com.k955.Coden.dtos.Bundle;

import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;

public record UpdateBundleRequest(
        String name,
        String description,
        BundleCategory bundleCategory,
        BundleStatus bundleStatus
) {
}
