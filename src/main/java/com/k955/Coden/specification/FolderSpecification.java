package com.k955.Coden.specification;

import com.k955.Coden.entity.Folder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FolderSpecification {

    public static Specification<Folder> filterBy(String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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
