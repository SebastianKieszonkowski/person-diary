package pl.kurs.persondiary.services.entityservices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.command.CreatePersonCommand;
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
    @Transactional
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

    public abstract void deleteAll();

    public T saveAndFlush(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public T updatePerson(T person, CreatePersonCommand update) {
        return null;
    }

    //    @Override
//    public T findByPesel(String pesel) {
//        return repository.findByPesel(pesel);//.orElseThrow(() -> new ResourceNotFoundException("Nie znalezion elementu"));
//    }
}