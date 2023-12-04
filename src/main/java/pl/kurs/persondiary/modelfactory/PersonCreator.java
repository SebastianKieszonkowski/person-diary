package pl.kurs.persondiary.modelfactory;

import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

public interface PersonCreator {
    String getType();
    IPersonDto createDtoFromView(PersonView personView);
}
