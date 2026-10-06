package com.k955.Coden.entity;

import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bundles")
public class Bundle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, length = 1000)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BundleStatus bundleStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BundleCategory bundleCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    User createdBy;

    @Builder.Default
    @OneToMany(
            mappedBy = "bundle",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<BundleFile> files = new ArrayList<>();

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

    public void addFile(BundleFile file) {
        files.add(file);
        file.setBundle(this);
    }

    public void removeFile(BundleFile file) {
        files.remove(file);
        file.setBundle(null);
    }

}
