package com.k955.Coden.specification;

import com.k955.Coden.entity.Bundle;
import com.k955.Coden.enums.Bundle.BundleCategory;
import com.k955.Coden.enums.Common.Language;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BundleSpecification {

    public static Specification<Bundle> filterBy(
            Language language, BundleCategory bundleCategory, String search
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(language != null) {
                predicates.add(cb.equal(root.get("language"), language));
            }

            if(bundleCategory != null) {
                predicates.add(cb.equal(root.get("bundleCategory"), bundleCategory));
            }

            if(search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("title")), pattern),
                                cb.like(cb.lower(root.get("description")), pattern)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
