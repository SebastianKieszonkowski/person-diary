package pl.kurs.persondiary.services.singleservice;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.repositories.singlerepositories.EmployeeRepository;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

@Service
public class EmployeeService extends AbstractGenericManagementService<Employee, EmployeeRepository> {

    public EmployeeService(EmployeeRepository repository) {
        super(repository);
    }
}
