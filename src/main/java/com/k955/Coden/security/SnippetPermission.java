package com.k955.Coden.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnippetPermission {
    EDIT("snippet:edit"),
    DELETE("snippet:delete");

    private final String value;
}
