package pl.kurs.persondiary.services.querybuilder;

import pl.kurs.persondiary.models.Person;

import java.util.Map;

public interface IQuery {
    String getType();
    Map<String, QueryConditionBuilder<Person>> getCondition();
}
