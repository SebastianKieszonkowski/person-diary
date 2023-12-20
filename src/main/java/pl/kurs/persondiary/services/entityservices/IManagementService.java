package pl.kurs.persondiary.services.entityservices;

import java.util.List;

public interface IManagementService<T> {

    T add(T entity);

    T edit(T entity);

    void delete(Long id);

    T get(Long id);

    List<T> getAll();

    String getType();

    void deleteAll();
}
