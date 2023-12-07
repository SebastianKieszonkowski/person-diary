package pl.kurs.persondiary.services.entityservices;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.EmployeePositionRepositories;

import java.util.Optional;

@Service
public class EmployeePositionService extends AbstractGenericManagementService<EmployeePosition, EmployeePositionRepositories> {

    public EmployeePositionService(EmployeePositionRepositories repository) {
        super(repository);
    }

    public Optional<EmployeePosition> findById(Long id) {
        return repository.findById(id);
    }

    //do usuniecia
    @Override
    public EmployeePosition findByPesel(String pesel) {
        return null;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public String getType() {
        return null;
    }
}
