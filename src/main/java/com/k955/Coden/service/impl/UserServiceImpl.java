package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.SuperAdmin.UpdateUserRole;
import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.BadRequestException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.UserMapper;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;

    @Override //TODO: replace super admin check with security expressions (NOT OPTIMAL)
    @Transactional
    public UserProfileResponse updateUserRole(UUID userId, UpdateUserRole updateUserRole) {
        UUID sadminId = jwtAuthUtil.getCurrentUserId();
        User sadmin = userRepository.findById(sadminId)
                .orElseThrow(() -> new ResourceNotFoundException(sadminId.toString(), "User"));

        if(!sadmin.getRole().equals(Role.SUPER_ADMIN)) throw new BadRequestException("Only Super Admins can update roles!");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        user.setRole(updateUserRole.role());
        User saved = userRepository.save(user);

        return userMapper.toUserProfileResponse(saved);
    }

}
