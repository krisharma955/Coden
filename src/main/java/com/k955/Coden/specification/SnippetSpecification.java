package com.k955.Coden.specification;

import com.k955.Coden.entity.Snippet;
import com.k955.Coden.enums.Snippet.Framework;
import com.k955.Coden.enums.Snippet.Language;
import com.k955.Coden.enums.Snippet.SnippetStatus;
import com.k955.Coden.enums.Snippet.SnippetType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SnippetSpecification {

    public static Specification<Snippet> filterBy(
            Language language, Framework framework, SnippetType snippetType,
            SnippetStatus snippetStatus, String search
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(language != null) {
                predicates.add(cb.equal(root.get("language"), language));
            }

            if(framework != null) {
                predicates.add(cb.equal(root.get("framework"), framework));
            }

            if(snippetType != null) {
                predicates.add(cb.equal(root.get("snippetType"), snippetType));
            }

            if(snippetStatus != null) {
                predicates.add(cb.equal(root.get("snippetStatus"), snippetStatus));
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
