package pl.kurs.persondiary.services.querybuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import pl.kurs.persondiary.models.PersonView;

import java.time.LocalDate;
import java.util.Map;

@Component
public class QueryFactoryComponent {

    private Map<String, QueryConditionBuilder<PersonView>> conditionBuilders;

    public QueryFactoryComponent(Map<String, QueryConditionBuilder<PersonView>> conditionBuilders) {
        this.conditionBuilders = conditionBuilders;

        conditionBuilders.put("type", (builder, root, value) -> builder.like(builder.lower(root.get("type")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("firstName", (builder, root, value) -> builder.like(builder.lower(root.get("firstName")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("lastName", (builder, root, value) -> builder.like(builder.lower(root.get("lastName")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("pesel", (builder, root, value) -> builder.like(root.get("pesel"), "%" + value.toString() + "%"));
        conditionBuilders.put("heightFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("height"), (Double) value));
        conditionBuilders.put("heightTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("height"), (Double) value));
        conditionBuilders.put("weightFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("weight"), (Double) value));
        conditionBuilders.put("weightTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("weight"), (Double) value));
        conditionBuilders.put("email", (builder, root, value) -> builder.like(builder.lower(root.get("email")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("versionFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("version"), (Integer) value));
        conditionBuilders.put("versionTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("version"), (Integer) value));
        conditionBuilders.put("gender", (builder, root, value) -> {
            if (value.toString().equalsIgnoreCase("female")) {
                return builder.like(builder.lower(root.get("firstName")), "%a");
            } else {
                return builder.notLike(builder.lower(root.get("firstName")), "%a");
            }
        });
        conditionBuilders.put("ageFrom", (builder, root, value) -> {
            LocalDate dateFrom = LocalDate.now().minusYears((Integer) value);
            return builder.lessThanOrEqualTo(root.get("birthDate"), dateFrom);
        });
        conditionBuilders.put("ageTo", (builder, root, value) -> {
            LocalDate dateTo = LocalDate.now().minusYears((Integer) value);
            return builder.greaterThanOrEqualTo(root.get("birthDate"), dateTo);
        });

        conditionBuilders.put("salaryFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("salary"), (Double) value));
        conditionBuilders.put("salaryTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("salary"), (Double) value));
        conditionBuilders.put("position", (builder, root, value) -> builder.like(builder.lower(root.get("position")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("universityName", (builder, root, value) -> builder.like(builder.lower(root.get("universityName")), "%" + value.toString().toLowerCase() + "%"));
        conditionBuilders.put("studyField", (builder, root, value) -> builder.like(builder.lower(root.get("studyField")), "%" + value.toString().toLowerCase() + "%"));

        conditionBuilders.put("studyYearFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("studyYear"), (Integer) value));
        conditionBuilders.put("studyYearTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("studyYear"), (Integer) value));
        conditionBuilders.put("scholarshipFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("scholarship"), (Double) value));
        conditionBuilders.put("scholarshipTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("scholarship"), (Double) value));

        conditionBuilders.put("workedYearsFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("workedYears"), (Integer) value));
        conditionBuilders.put("workedYearsTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("workedYears"), (Integer) value));
        conditionBuilders.put("pensionFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("pension"), (Double) value));
        conditionBuilders.put("pensionTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("pension"), (Double) value));

    }

    public Predicate buildPredicate(String fieldName, CriteriaBuilder builder, Root<PersonView> root, Object value) {
        QueryConditionBuilder<PersonView> queryConditionBuilder = conditionBuilders.get(fieldName);
        if (queryConditionBuilder != null) {
            return queryConditionBuilder.build(builder, root, value);
        }
        throw new IllegalArgumentException("No query condition builder found for field: " + fieldName);
    }
}
