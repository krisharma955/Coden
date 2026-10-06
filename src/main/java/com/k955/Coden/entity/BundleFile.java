package com.k955.Coden.entity;

import com.k955.Coden.enums.Common.Language;
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
        name = "bundle_files",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bundle_file_path",
                columnNames = {"bundle_id", "file_path"}
        )
)
public class BundleFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id", nullable = false)
    Bundle bundle;

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false, unique = true)
    String objectKey;

    @Column(nullable = false, name = "file_path", length = 1000)
    String filePath;

    @Column(nullable = false)
    long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Language language;

    @Column(nullable = false)
    String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    User uploadedBy;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

    @PrePersist
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
