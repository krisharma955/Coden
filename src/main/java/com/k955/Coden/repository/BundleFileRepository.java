package com.k955.Coden.repository;

import com.k955.Coden.entity.BundleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BundleFileRepository extends JpaRepository<BundleFile, UUID> {

    boolean existsByBundleIdAndFilePath(UUID bundleId, String filePath);

    Optional<BundleFile> findByIdAndBundleId(UUID fileId, UUID bundleId);

    Optional<BundleFile> findByBundleId(UUID bundleId);

}
