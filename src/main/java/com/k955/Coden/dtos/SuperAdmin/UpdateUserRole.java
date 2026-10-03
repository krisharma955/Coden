package com.k955.Coden.dtos.SuperAdmin;

import com.k955.Coden.enums.User.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRole(
        @NotNull Role role
) {
}
