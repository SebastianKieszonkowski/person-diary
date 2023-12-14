package pl.kurs.persondiary.services.entityservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.singlerepositories.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService extends AbstractGenericManagementService<Employee, EmployeeRepository> {
    private final EmployeePositionService employeePositionService;

    public EmployeeService(EmployeeRepository repository, EmployeePositionService employeePositionService) {
        super(repository);
        this.employeePositionService = employeePositionService;
    }

    @Override
    @Transactional
    public Employee add(Employee entity) {
        Employee employee = super.add(entity);
        EmployeePosition employeePosition = new EmployeePosition(employee.getPosition(),
                employee.getHireDate(),
                null,
                employee.getSalary(),
                employee);
        EmployeePosition employeePositionCreated = employeePositionService.add(employeePosition);
        return employee;
    }

    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }


    public Employee findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Employee findByPesel(String pesel) {
        return repository.findByPesel(pesel).orElseThrow();
    }

    @Override
    public Employee updatePerson(Employee person, CreatePersonCommand update) {
        return super.updatePerson(person, update);
    }
}
