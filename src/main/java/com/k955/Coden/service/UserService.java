package com.k955.Coden.service;

import com.k955.Coden.dtos.SuperAdmin.UpdateUserRole;
import com.k955.Coden.dtos.User.UserProfileResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserService {

    UserProfileResponse updateUserRole(UUID userId, @Valid UpdateUserRole updateUserRole);

    UserProfileResponse getUserByEmail(String email);

    UserProfileResponse updateUserRoleByEmail(String email, @Valid UpdateUserRole updateUserRole);

}
