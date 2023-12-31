package pl.kurs.persondiary.services.querybuilder;

import org.springframework.stereotype.Component;
import pl.kurs.persondiary.factory.PersonCreator;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;

import java.time.LocalDate;
import java.util.Map;

@Component
public class QueryPersonBuilderComponent implements IQuery {

    private final Map<String, QueryConditionBuilder<Person>> conditionBuilders;
    private final PersonFactory personFactory;

    public QueryPersonBuilderComponent(Map<String, QueryConditionBuilder<Person>> conditionBuilders, PersonFactory personFactory) {
        this.conditionBuilders = conditionBuilders;
        this.personFactory = personFactory;
        init();
    }

    private void init() {
        conditionBuilders.put("type", (builder, root, value) ->{
            PersonCreator personCreator = personFactory.getCreator(value);
            return builder.equal(root.type(), personCreator.getEntityClass());
        });
        conditionBuilders.put("firstName", (builder, root, value) -> builder.like(builder.lower(root.get("firstName")), "%" + value.toLowerCase() + "%"));
        conditionBuilders.put("lastName", (builder, root, value) -> builder.like(builder.lower(root.get("lastName")), "%" + value.toLowerCase() + "%"));
        conditionBuilders.put("pesel", (builder, root, value) -> builder.like(root.get("pesel"), "%" + value + "%"));
        conditionBuilders.put("heightFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("height"), Double.parseDouble(value)));
        conditionBuilders.put("heightTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("height"), Double.parseDouble(value)));
        conditionBuilders.put("weightFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("weight"), Double.parseDouble(value)));
        conditionBuilders.put("weightTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("weight"), Double.parseDouble(value)));
        conditionBuilders.put("email", (builder, root, value) -> builder.like(builder.lower(root.get("email")), "%" + value.toLowerCase() + "%"));
        conditionBuilders.put("versionFrom", (builder, root, value) -> builder.greaterThanOrEqualTo(root.get("version"), Integer.parseInt(value)));
        conditionBuilders.put("versionTo", (builder, root, value) -> builder.lessThanOrEqualTo(root.get("version"), Integer.parseInt(value)));
        conditionBuilders.put("gender", (builder, root, value) -> {
            if (value.equalsIgnoreCase("female")) {
                return builder.like(builder.lower(root.get("firstName")), "%a");
            } else {
                return builder.notLike(builder.lower(root.get("firstName")), "%a");
            }
        });
        conditionBuilders.put("ageFrom", (builder, root, value) -> {
            LocalDate dateFrom = LocalDate.now().minusYears(Integer.parseInt(value));
            return builder.lessThanOrEqualTo(root.get("birthdate"), dateFrom);
                });
        conditionBuilders.put("ageTo", (builder, root, value) -> {
            LocalDate dateTo = LocalDate.now().minusYears(Integer.parseInt(value));
            return builder.greaterThanOrEqualTo(root.get("birthdate"), dateTo);
        });
    }

    @Override
    public String getType() {
        return "person";
    }

    @Override
    public Map<String, QueryConditionBuilder<Person>> getCondition() {
        return conditionBuilders;
    }

}
