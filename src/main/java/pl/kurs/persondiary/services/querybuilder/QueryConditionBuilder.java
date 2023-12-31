package pl.kurs.persondiary.services.querybuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface QueryConditionBuilder<T> {
    Predicate build(CriteriaBuilder builder, Root<T> root, String value);
}
