package com.k955.Coden.enums.User;

import com.k955.Coden.security.SnippetPermission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.k955.Coden.security.SnippetPermission.DELETE;
import static com.k955.Coden.security.SnippetPermission.EDIT;

@Getter
@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN(EDIT, DELETE),
    ADMIN(EDIT),
    USER;

    Role(SnippetPermission... permissions) {
        this.permissions = Set.of(permissions);
    }

    private final Set<SnippetPermission> permissions;
}
