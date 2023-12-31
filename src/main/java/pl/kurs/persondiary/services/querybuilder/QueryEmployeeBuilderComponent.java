package pl.kurs.persondiary.services.querybuilder;

import org.springframework.stereotype.Component;
import pl.kurs.persondiary.models.Person;

import java.util.Map;

@Component
public class QueryEmployeeBuilderComponent implements IQuery {

    private final Map<String, QueryConditionBuilder<Person>> conditionBuilders;

    public QueryEmployeeBuilderComponent(Map<String, QueryConditionBuilder<Person>> conditionBuilders) {
        this.conditionBuilders = conditionBuilders;
        init();
    }

    private void init() {
        conditionBuilders.put("salaryFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("salary"), Double.parseDouble(value)));
        conditionBuilders.put("salaryTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("salary"), Double.parseDouble(value)));
        conditionBuilders.put("position", (builder, root, value) -> builder.like(builder.lower(root.get("position")), "%" + value.toLowerCase() + "%"));
    }

    @Override
    public String getType() {
        return "employee";
    }

    @Override
    public Map<String, QueryConditionBuilder<Person>> getCondition() {
        return conditionBuilders;
    }

}
