package pl.kurs.persondiary.services.entityservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.IncorrectDateRangeException;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.EmployeeRepository;

import java.util.Optional;

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
        employeePositionService.add(employeePosition);
        return employee;
    }

    @Override
    public Employee edit(Employee entity) {
        Employee employee = super.add(entity);
        EmployeePosition employeePosition = new EmployeePosition(employee.getPosition(),
                employee.getHireDate(),
                null,
                employee.getSalary(),
                employee);
        employeePositionService.editActualPosition(employeePosition);
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Transactional(readOnly = true)
    public Employee findPersonByPeselWithPosition(String pesel) {
        return repository.getByPeselWithPositions(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Transactional(readOnly = true)
    public void checkPeriodPositionConflict(EmployeePosition position) {
        if (position.getEndDateOnPosition().isBefore(position.getStartDateOnPosition()))
            throw new IncorrectDateRangeException("The end date of the position is earlier than the end date!!!");
        if (!employeePositionService.checkDates(position.getStartDateOnPosition(), position.getEndDateOnPosition()
                , position.getEmployee().getId()).isEmpty())
            throw new IncorrectDateRangeException("The specified working period coincides with the existing ones!!!");
    }

    public EmployeePosition addPositionToEmployee(EmployeePosition position) {
        return employeePositionService.add(position);
    }

    @Transactional
    public EmployeePosition updatePosition(EmployeePosition modifyPosition) {
        EmployeePosition existPosition = employeePositionService.findById(modifyPosition.getId());

        Optional.ofNullable(modifyPosition.getPositionName()).ifPresent(existPosition::setPositionName);
        Optional.ofNullable(modifyPosition.getStartDateOnPosition()).ifPresent(existPosition::setStartDateOnPosition);
        Optional.ofNullable(modifyPosition.getEndDateOnPosition()).ifPresent(existPosition::setEndDateOnPosition);
        Optional.ofNullable(modifyPosition.getSalary()).ifPresent(existPosition::setSalary);

        return employeePositionService.add(existPosition);
    }

}
