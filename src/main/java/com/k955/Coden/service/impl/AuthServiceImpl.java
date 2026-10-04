package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Auth.AuthResponse;
import com.k955.Coden.dtos.Auth.LoginRequest;
import com.k955.Coden.dtos.Auth.SignupRequest;
import com.k955.Coden.entity.User;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.UserMapper;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtAuthUtil jwtAuthUtil;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest signupRequest) {
        boolean check = userRepository.existsByEmail(signupRequest.email());
        if(check) throw new BadRequestException("User already exists with Email: " + signupRequest.email());

        User user = User.builder()
                .name(signupRequest.name())
                .username(signupRequest.username())
                .email(signupRequest.email())
                .password(signupRequest.password())
                .build();
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        User saved = userRepository.save(user);

        String token = jwtAuthUtil.generateAccessToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponse(saved));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid Email or Password");
        }

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException(loginRequest.email(), "User"));

        String token = jwtAuthUtil.generateAccessToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

}
