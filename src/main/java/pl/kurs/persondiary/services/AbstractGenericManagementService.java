package pl.kurs.persondiary.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.exeptions.MissingIdException;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Identificationable;

import java.util.List;

public abstract class AbstractGenericManagementService<T extends Identificationable,
        R extends JpaRepository<T, Long>> implements IManagementService<T> {

    protected final R repository;

    public AbstractGenericManagementService(R repository) {
        this.repository = repository;
    }

    @Override
    public T add(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public T edit(T entity) {
        if (entity.getId() == null)
            throw new MissingIdException("Brak Id w encji do edycji!!!");
        return repository.save(entity);
    }

    @Override
    public T get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono encji o id: " + id));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();

    }
}