package com.k955.Coden.entity;

import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "snippets")
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Language language;

    @Enumerated(EnumType.STRING)
    Framework framework;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SnippetType snippetType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SnippetStatus snippetStatus = SnippetStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    User reviewedBy;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

    Instant deletedAt;

    @PrePersist //now jpa sets timestamp before insertion
    protected void onCreate() {
        Instant now = Instant.now();
        if(createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}
