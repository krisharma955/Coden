package com.k955.Coden.specification;

import com.k955.Coden.entity.SnippetStar;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SnippetStarSpecification {

    public static Specification<SnippetStar> filterBy(String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("snippet").get("title")), pattern),
                                cb.like(cb.lower(root.get("snippet").get("description")), pattern)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
