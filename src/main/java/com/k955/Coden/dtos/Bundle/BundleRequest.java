package com.k955.Coden.dtos.Bundle;

import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BundleRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull BundleStatus bundleStatus,
        @NotNull BundleCategory bundleCategory
) {
}
