package pl.kurs.persondiary.services;

import java.util.List;

public interface IManagementService<T> {

    T add(T entity);

    void delete(Long id);

    T edit(T entity);

    T get(Long id);

    List<T> getAll();

    String getType();

}
