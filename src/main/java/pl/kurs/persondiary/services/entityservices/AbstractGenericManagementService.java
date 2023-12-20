package pl.kurs.persondiary.services.entityservices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public T add(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public T edit(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public T get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteAll(){
        repository.deleteAll();
    }


}