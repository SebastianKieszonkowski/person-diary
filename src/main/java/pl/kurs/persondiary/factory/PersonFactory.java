package pl.kurs.persondiary.factory;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.UpdatePersonCommand;
import pl.kurs.persondiary.dto.IFullPersonDto;
import pl.kurs.persondiary.dto.ISimplePersonDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonFactory {

    private final Map<String, PersonCreator> creators;

    public PersonFactory(Set<PersonCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(PersonCreator::getType, Function.identity()));
    }

    public Person create(CreatePersonCommand command) {
        return creators.get(command.getType().toLowerCase(Locale.ROOT)).create(command.getParameters());
    }

    public Person update(Person person, UpdatePersonCommand command) {
        return creators.get(command.getType().toLowerCase(Locale.ROOT)).update(person, command.getParameters());
    }

    public IFullPersonDto createDtoFromView(PersonView personView) {
        return creators.get(personView.getType().toLowerCase(Locale.ROOT)).createDtoFromView(personView);
    }

    public ISimplePersonDto createSimpleDtoFromView(PersonView personView) {
        return creators.get(personView.getType().toLowerCase(Locale.ROOT)).createSimpleDtoFromView(personView);
    }

    public IFullPersonDto createDtoFromPerson(Person person) {
        return creators.get(person.getClass().getSimpleName().toLowerCase(Locale.ROOT)).createDtoFromPerson(person);
    }

    public Person createPersonFromView(PersonView personView) {
        return creators.get(personView.getType().toLowerCase(Locale.ROOT)).createPersonFromView(personView);
    }
}

