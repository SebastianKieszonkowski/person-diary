package pl.kurs.persondiary.services.querybuilder;

import org.springframework.stereotype.Component;
import pl.kurs.persondiary.models.Person;

import java.util.Map;

@Component
public class QueryStudentBuilderComponent implements IQuery {

    private final Map<String, QueryConditionBuilder<Person>> conditionBuilders;

    public QueryStudentBuilderComponent(Map<String, QueryConditionBuilder<Person>> conditionBuilders) {
        this.conditionBuilders = conditionBuilders;
        init();
    }

    private void init() {
        conditionBuilders.put("studyYearFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("studyYear"), Integer.parseInt(value)));
        conditionBuilders.put("studyYearTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("studyYear"), Integer.parseInt(value)));
        conditionBuilders.put("scholarshipFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("scholarship"), Double.parseDouble(value)));
        conditionBuilders.put("scholarshipTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("scholarship"), Double.parseDouble(value)));
        conditionBuilders.put("universityName", (builder, root, value) -> builder.like(builder.lower(root.get("universityName")), "%" + value.toLowerCase() + "%"));
        conditionBuilders.put("studyField", (builder, root, value) -> builder.like(builder.lower(root.get("studyField")), "%" + value.toLowerCase()));
    }

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public Map<String, QueryConditionBuilder<Person>> getCondition() {
        return conditionBuilders;
    }

}
