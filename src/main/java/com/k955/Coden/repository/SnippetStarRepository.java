package com.k955.Coden.repository;

import com.k955.Coden.entity.SnippetStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SnippetStarRepository extends JpaRepository<SnippetStar, UUID>, JpaSpecificationExecutor<SnippetStar> {

    boolean existsBySnippetIdAndUserId(UUID snippetId, UUID userId);

    Optional<SnippetStar> findBySnippetIdAndUserId(UUID snippetId, UUID userId);

}
