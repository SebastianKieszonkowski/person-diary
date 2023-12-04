package pl.kurs.persondiary.modelfactory;

import com.fasterxml.jackson.databind.JsonNode;
import pl.kurs.persondiary.command.ICreatePersonCommand;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

public interface PersonCreator {
    String getType();
    IPersonDto createDtoFromView(PersonView personView);
    Person createPerson(JsonNode personJsonNode);
    IPersonDto createDtoFromPerson(Person person);
}
