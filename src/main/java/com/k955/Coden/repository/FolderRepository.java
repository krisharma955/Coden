package com.k955.Coden.repository;

import com.k955.Coden.entity.Folder;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID>, JpaSpecificationExecutor<Folder> {

    boolean existsByName(@NotBlank String name);

}
