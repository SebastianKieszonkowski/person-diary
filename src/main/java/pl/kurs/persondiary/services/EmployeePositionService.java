package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.EmployeePositionRepositories;

@Service
public class EmployeePositionService extends AbstractGenericManagementService<EmployeePosition, EmployeePositionRepositories> {
    public EmployeePositionService(EmployeePositionRepositories repository) {
        super(repository);
    }
}
