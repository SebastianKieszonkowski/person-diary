package pl.kurs.persondiary.services.personservices;

import java.util.List;

public interface IManagementService<T> {

    T add(T entity);

    T edit(T entity);

    T findByPesel(String pesel);

    void delete(Long id);

    T get(Long id);

    List<T> getAll();

    String getType();

    void deleteAll();

    boolean existsByPesel(String pesel);

    Long getSize();
}
