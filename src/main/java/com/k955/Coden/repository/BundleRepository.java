package com.k955.Coden.repository;

import com.k955.Coden.entity.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BundleRepository extends JpaRepository<Bundle, UUID>, JpaSpecificationExecutor<Bundle> {

    boolean existsByName(String name);

}
