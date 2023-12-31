package pl.kurs.persondiary.services.querybuilder;

import org.springframework.stereotype.Component;
import pl.kurs.persondiary.models.Person;

import java.util.Map;

@Component
public class QueryPensionerBuilderComponent implements IQuery{

    private final Map<String, QueryConditionBuilder<Person>> conditionBuilders;

    public QueryPensionerBuilderComponent(Map<String, QueryConditionBuilder<Person>> conditionBuilders) {
        this.conditionBuilders = conditionBuilders;
        init();
    }

    private void init(){
        conditionBuilders.put("workedYearsFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("workedYears"), Integer.parseInt(value)));
        conditionBuilders.put("workedYearsTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("workedYears"), Integer.parseInt(value)));
        conditionBuilders.put("pensionFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("pension"), Double.parseDouble(value)));
        conditionBuilders.put("pensionTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("pension"), Double.parseDouble(value)));
    }

    @Override
    public String getType() {
        return "pensioner";
    }

    @Override
    public Map<String, QueryConditionBuilder<Person>> getCondition() {
        return conditionBuilders;
    }

}
