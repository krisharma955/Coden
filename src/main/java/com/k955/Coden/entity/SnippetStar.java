package com.k955.Coden.entity;

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
@Table(
        name = "snippet_stars",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "snippet_id"})
)
public class SnippetStar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snippet_id", nullable = false)
    Snippet snippet;

    @Column(nullable = false, updatable = false)
    Instant starredAt;

    @PrePersist
    protected void onStar() {
        Instant now = Instant.now();
        if(starredAt == null) starredAt = now;
    }

}
