package pl.kurs.persondiary.services.singleservice;

import pl.kurs.persondiary.models.Person;

public interface IPersonService {
    String getType();
    IPersonService getPersonService(Person person);
}
