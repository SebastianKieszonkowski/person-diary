package pl.kurs.persondiary.factory;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.CreateEmployeeCommand;
import pl.kurs.persondiary.dto.FullEmployeeDto;
import pl.kurs.persondiary.dto.IFullPersonDto;
import pl.kurs.persondiary.dto.ISimplePersonDto;
import pl.kurs.persondiary.dto.SimpleEmployeeDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

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
        CreateEmployeeCommand createEmployeeCommand = new CreateEmployeeCommand(
                getStringParameter("firstName", parameters),
                getStringParameter("lastName", parameters),
                getStringParameter("pesel", parameters),
                getDoubleParameter("height", parameters),
                getDoubleParameter("weight", parameters),
                getStringParameter("email", parameters),
                getLocalDataParameter("hireDate", parameters),
                getStringParameter("position", parameters),
                getDoubleParameter("salary", parameters));

        commandValidator(createEmployeeCommand, (org.springframework.validation.Validator) validator);
        return modelMapper.map(createEmployeeCommand, Employee.class);
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Employee employee = (Employee) person;
        Optional.ofNullable(getStringParameter("firstName", parameters)).ifPresent(employee::setFirstName);
        Optional.ofNullable(getStringParameter("lastName", parameters)).ifPresent(employee::setLastName);
        Optional.ofNullable(getStringParameter("pesel", parameters)).ifPresent(employee::setPesel);
        Optional.ofNullable(getDoubleParameter("height", parameters)).ifPresent(employee::setHeight);
        Optional.ofNullable(getDoubleParameter("weight", parameters)).ifPresent(employee::setWeight);
        Optional.ofNullable(getStringParameter("email", parameters)).ifPresent(employee::setEmail);
        Optional.ofNullable(getIntegerParameter("version", parameters)).ifPresent(employee::setVersion);
        Optional.ofNullable(getLocalDataParameter("hireDate", parameters)).ifPresent(employee::setHireDate);
        Optional.ofNullable(getStringParameter("position", parameters)).ifPresent(employee::setPosition);
        Optional.ofNullable(getDoubleParameter("salary", parameters)).ifPresent(employee::setSalary);
        return employee;
    }

    @Override
    public IFullPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, FullEmployeeDto.class);
    }

    @Override
    public ISimplePersonDto createSimpleDtoFromView(PersonView personView) {
        return modelMapper.map(personView, SimpleEmployeeDto.class);
    }

    @Override
    public IFullPersonDto createDtoFromPerson(Person person) {
        Employee employee = (Employee) person;
        return modelMapper.map(employee, FullEmployeeDto.class);
    }

    @Override
    public Person createPersonFromView(PersonView personView) {
        return modelMapper.map(personView, Employee.class);
    }

}
