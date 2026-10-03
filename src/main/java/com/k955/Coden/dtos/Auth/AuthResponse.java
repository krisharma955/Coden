package com.k955.Coden.dtos.Auth;

import com.k955.Coden.dtos.User.UserProfileResponse;

public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
