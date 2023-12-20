package pl.kurs.persondiary.services.entityservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.singlerepositories.EmployeeRepository;

@Service
public class EmployeeService extends AbstractGenericManagementService<Employee, EmployeeRepository> {
    private final EmployeePositionService employeePositionService;

    public EmployeeService(EmployeeRepository repository, EmployeePositionService employeePositionService) {
        super(repository);
        this.employeePositionService = employeePositionService;
    }

    @Override
    public String getType() {
        return "employee";
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
    @Transactional
    public Employee edit(Employee entity) {
        Employee employee = super.add(entity);
        EmployeePosition employeePosition = new EmployeePosition(employee.getPosition(),
                employee.getHireDate(),
                null,
                employee.getSalary(),
                employee);
        EmployeePosition employeePositionCreated = employeePositionService.editActualPosition(employeePosition);
        return employee;
    }

    public Employee findPersonByPesel(String pesel){
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }
}
