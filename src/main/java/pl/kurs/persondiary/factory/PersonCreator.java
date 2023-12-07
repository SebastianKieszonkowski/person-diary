package pl.kurs.persondiary.factory;

import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.time.LocalDate;
import java.util.Map;

public interface PersonCreator {
    String getType();
    Person create(Map<String, Object> parameters);
    Person update(Person person, Map<String, Object> parameters);
    IPersonDto createDtoFromView(PersonView personView);
    IPersonDto createDtoFromPerson(Person person);


    default String getStringParameter(String name, Map<String, Object> parameters){
        return (String) parameters.get(name);
    }

    default Integer getIntegerParameter(String name, Map<String,Object> parameters){
        return (Integer) parameters.get(name);
    }

    default Double getDoubleParameter(String name, Map<String,Object> parameters){
        return (Double) parameters.get(name);
    }

    default LocalDate getLocalDataParameter(String name, Map<String,Object> parameters){
        return LocalDate.parse(parameters.get(name).toString());
    }
}
