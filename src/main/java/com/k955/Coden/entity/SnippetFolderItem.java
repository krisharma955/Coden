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
        name = "snippet_folder_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"folder_id", "snippet_id"})
)
public class SnippetFolderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snippet_id", nullable = false)
    Snippet snippet;

    @Column(nullable = false, updatable = false)
    Instant addedAt;

    @PrePersist
    protected void onAddition() {
        Instant now = Instant.now();
        if(addedAt == null) addedAt = now;
    }

}
