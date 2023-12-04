package pl.kurs.persondiary.services;

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
        return Optional.ofNullable(super.get(id));
    }

    @Override
    public void deleteAll() {
        super.repository.deleteAll();
    }
}
