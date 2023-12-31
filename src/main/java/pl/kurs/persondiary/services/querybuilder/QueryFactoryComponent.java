package pl.kurs.persondiary.services.querybuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import pl.kurs.persondiary.models.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class QueryFactoryComponent {

    private final Map<String, QueryConditionBuilder<Person>> conditionBuilders = new HashMap<>();
    private final Map<String, IQuery> creators;

    public QueryFactoryComponent(Set<IQuery> creatorSet) {
        this.creators = initializeCreators(creatorSet);
        initializeConditionBuilders();
    }

    private Map<String, IQuery> initializeCreators(Set<IQuery> creators) {
        return creators.stream().collect(Collectors.toMap(IQuery::getType, Function.identity()));
    }

    private void initializeConditionBuilders() {
        creators.values().stream()
                .map(IQuery::getCondition)
                .flatMap(map -> map.entrySet().stream())
                .forEach(e -> conditionBuilders.put(e.getKey(), e.getValue()));
    }

    public Optional<Predicate> buildPredicate(String fieldName, CriteriaBuilder builder, Root<Person> root, String value) {
        QueryConditionBuilder<Person> queryConditionBuilder = conditionBuilders.get(fieldName);
        if (queryConditionBuilder != null) {
            return Optional.of(queryConditionBuilder.build(builder, root, value));
        }
        return Optional.empty();
    }
}
