package pl.kurs.persondiary.services.entityservices;

import pl.kurs.persondiary.command.CreatePersonCommand;

import java.util.List;

public interface IManagementService<T> {

    T add(T entity);

    void delete(Long id);

    T edit(T entity);

    T get(Long id);

    List<T> getAll();

    String getType();

    T findByPesel(String pesel);

    T updatePerson(T person, CreatePersonCommand update);

    T saveAndFlush(T entity);

}
