package pl.kurs.persondiary.factory;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.personcreate.CreateEmployeeCommand;
import pl.kurs.persondiary.command.personupdate.UpdateEmployeeCommand;
import pl.kurs.persondiary.dto.fulldto.FullEmployeeDto;
import pl.kurs.persondiary.dto.fulldto.IFullPersonDto;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.dto.simpledto.SimpleEmployeeDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Person;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final Validator validator;

    @Override
    public String getType() {
        return "employee";
    }

    @Override
    public Person create(Map<String, Object> parameters) {
        CreateEmployeeCommand createEmployeeCommand = new CreateEmployeeCommand(getStringParameter("firstName", parameters)
                , getStringParameter("lastName", parameters)
                , getStringParameter("pesel", parameters)
                , getDoubleParameter("height", parameters)
                , getDoubleParameter("weight", parameters)
                , getStringParameter("email", parameters)
                , getBirthdateFromPesel(getStringParameter("pesel", parameters))
                , getLocalDataParameter("hireDate", parameters)
                , getStringParameter("position", parameters)
                , getDoubleParameter("salary", parameters));

        commandValidator(createEmployeeCommand, (org.springframework.validation.Validator) validator);
        return modelMapper.map(createEmployeeCommand, Employee.class);
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Employee employee = (Employee) person;

        UpdateEmployeeCommand updateEmployee = new UpdateEmployeeCommand(
                Optional.ofNullable(getStringParameter("firstName", parameters)).orElse(employee.getFirstName()),
                Optional.ofNullable(getStringParameter("lastName", parameters)).orElse(employee.getLastName()),
                Optional.ofNullable(getDoubleParameter("height", parameters)).orElse(employee.getHeight()),
                Optional.ofNullable(getDoubleParameter("weight", parameters)).orElse(employee.getWeight()),
                Optional.ofNullable(getStringParameter("email", parameters)).orElse(employee.getEmail()),
                Optional.ofNullable(getLocalDataParameter("hireDate", parameters)).orElse(employee.getHireDate()),
                Optional.ofNullable(getStringParameter("position", parameters)).orElse(employee.getPosition()),
                Optional.ofNullable(getDoubleParameter("salary", parameters)).orElse(employee.getSalary()));

        commandValidator(updateEmployee, (org.springframework.validation.Validator) validator);

        employee.setFirstName(updateEmployee.getFirstName());
        employee.setLastName(updateEmployee.getLastName());
        employee.setHeight(updateEmployee.getHeight());
        employee.setWeight(updateEmployee.getWeight());
        employee.setEmail(updateEmployee.getEmail());
        employee.setHireDate(updateEmployee.getHireDate());
        employee.setPosition(updateEmployee.getPosition());
        employee.setSalary(updateEmployee.getSalary());

        return employee;
    }

    @Override
    public ISimplePersonDto createSimpleDtoFromPerson(Person person) {
        Employee employee = (Employee) person;
        return modelMapper.map(employee, SimpleEmployeeDto.class);
    }

    @Override
    public IFullPersonDto createDtoFromPerson(Person person) {
        Employee employee = (Employee) person;
        return modelMapper.map(employee, FullEmployeeDto.class);
    }

    @Override
    public Object getEntityClass() {
        return Employee.class;
    }
}
