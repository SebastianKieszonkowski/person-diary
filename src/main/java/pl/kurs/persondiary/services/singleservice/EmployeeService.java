package pl.kurs.persondiary.services.singleservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.singlerepositories.EmployeeRepository;
import pl.kurs.persondiary.services.AbstractGenericManagementService;
import pl.kurs.persondiary.services.EmployeePositionService;

import java.util.List;

@Service
public class EmployeeService extends AbstractGenericManagementService<Employee, EmployeeRepository> {
    private final EmployeePositionService employeePositionService;

    public EmployeeService(EmployeeRepository repository, EmployeePositionService employeePositionService) {
        super(repository);
        this.employeePositionService = employeePositionService;
    }

//    @Transactional(readOnly = true)
//    public List<Employee> findAllPageable(Pageable pageable) {
//        int startPosition = pageable.getPageNumber() * pageable.getPageSize();
//        int stopPosition = (pageable.getPageNumber() + 1) * pageable.getPageSize();
//        return super.repository.findAll(startPosition, stopPosition);
//    }


    @Override
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
    public void deleteAll() {
        repository.deleteAll();
    }


    public Employee findById(Long id){
        return repository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public String getType() {
        return "EMPLOYEE";
    }
}
