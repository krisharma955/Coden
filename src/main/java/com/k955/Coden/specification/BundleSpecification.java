package com.k955.Coden.specification;

import com.k955.Coden.entity.Bundle;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Bundle.BundleStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BundleSpecification {

    public static Specification<Bundle> filterBy(
            BundleCategory bundleCategory, String search, boolean includeUnpublished
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(!includeUnpublished) {
                predicates.add(cb.equal(root.get("bundleStatus"), BundleStatus.PUBLISHED));
            }

            if(bundleCategory != null) {
                predicates.add(cb.equal(root.get("bundleCategory"), bundleCategory));
            }

            if(search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), pattern),
                                cb.like(cb.lower(root.get("description")), pattern)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
