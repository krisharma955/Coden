package com.k955.Coden.service;

import com.k955.Coden.dtos.Auth.AuthResponse;
import com.k955.Coden.dtos.Auth.LoginRequest;
import com.k955.Coden.dtos.Auth.SignupRequest;
import com.k955.Coden.dtos.User.UserProfileResponse;
import jakarta.validation.Valid;

public interface AuthService {

    AuthResponse signup(@Valid SignupRequest signupRequest);

    AuthResponse login(@Valid LoginRequest loginRequest);

    UserProfileResponse getCurrentUser();

}
