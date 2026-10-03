package com.k955.Coden.dtos.Auth;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password
) {
}
