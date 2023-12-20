package pl.kurs.persondiary.factory;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import pl.kurs.persondiary.command.ICreatePersonCommand;
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

    Person createPersonFromView(PersonView personView);


    default String getStringParameter(String name, Map<String, Object> parameters) {
        return (String) parameters.get(name);
    }

    default Integer getIntegerParameter(String name, Map<String, Object> parameters) {
        return (Integer) parameters.get(name);
    }

    default Double getDoubleParameter(String name, Map<String, Object> parameters) {
        return (Double) parameters.get(name);
    }

    default LocalDate getLocalDataParameter(String name, Map<String, Object> parameters) {
        LocalDate date = null;
        if (parameters.get(name) != null)
            date = LocalDate.parse(parameters.get(name).toString());
        return date;
    }

    default void commandValidator(ICreatePersonCommand createPersonCommand, Validator validator){
        DataBinder binder = new DataBinder(createPersonCommand);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid validation: " + bindingResult.toString());
        }
    }
}
