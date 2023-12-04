package pl.kurs.persondiary.modelfactory;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.ICreatePersonCommand;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.dto.viewdto.PensionerViewDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.*;
import pl.kurs.persondiary.services.creators.EmployeeServiceCreator;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmployeeCreator implements PersonCreator {
    private final ModelMapper modelMapper;
    private final EmployeeServiceCreator employeeServiceCreator;

    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, EmployeeViewDto.class);
    }

    @Override
    public Person createPerson(JsonNode personJsonNode) {
        CreateEmployeeCommand createEmployeeCommand = employeeServiceCreator.prepareEmployeeCommand(personJsonNode);
        return employeeServiceCreator.createEmployee(createEmployeeCommand);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Employee employee = (Employee)person;
        return modelMapper.map(employee, EmployeeViewDto.class);
    }
}
