package com.k955.Coden.specification;

import com.k955.Coden.entity.Notification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<Notification> filterBy(boolean isRead) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("isRead"), isRead));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
