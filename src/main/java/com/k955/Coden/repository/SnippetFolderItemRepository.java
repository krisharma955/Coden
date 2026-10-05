package com.k955.Coden.repository;

import com.k955.Coden.entity.SnippetFolderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SnippetFolderItemRepository extends JpaRepository<SnippetFolderItem, UUID> {

    boolean existsByFolderIdAndSnippetId(UUID folderId, UUID snippetId);

    Page<SnippetFolderItem> findByFolderId(UUID folderId, Pageable pageable);

    void deleteByFolderIdAndSnippetId(UUID folderId, UUID snippetId);

}
