package com.k955.Coden.security;

import com.k955.Coden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;

    public boolean hasPermission(UUID snippetId, SnippetPermission snippetPermission) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        return userRepository.findRoleById(userId)
                .map(role -> role.getPermissions().contains(snippetPermission))
                .orElse(false);
    }

    public boolean canEditSnippet(UUID snippetId) {
        return hasPermission(snippetId, SnippetPermission.EDIT);
    }

    public boolean canDeleteSnippet(UUID snippetId) {
        return hasPermission(snippetId, SnippetPermission.DELETE);
    }

}
