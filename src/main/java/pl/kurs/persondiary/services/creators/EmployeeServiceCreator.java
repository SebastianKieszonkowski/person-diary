package pl.kurs.persondiary.services.creators;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.models.Employee;

import java.time.LocalDate;

@Service
@Validated
public class EmployeeServiceCreator {

    public CreateEmployeeCommand prepareEmployeeCommand(JsonNode personJsonNode){
        return CreateEmployeeCommand.builder()
                .type(personJsonNode.get("type").asText())
                .firstName(personJsonNode.get("firstName").asText())
                .lastName(personJsonNode.get("lastName").asText())
                .pesel(personJsonNode.get("pesel").asText())
                .height(personJsonNode.get("height").asDouble())
                .weight(personJsonNode.get("weight").asDouble())
                .email(personJsonNode.get("email").asText())
                .hireDate(LocalDate.parse(personJsonNode.get("hireDate").asText()))
                .position(personJsonNode.get("position").asText())
                .salary(personJsonNode.get("salary").asDouble())
                .build();
    }
    public Employee createEmployee(@Valid CreateEmployeeCommand createEmployeeCommand) {
        return Employee.builder()
                .firstName(createEmployeeCommand.getFirstName())
                .lastName(createEmployeeCommand.getLastName())
                .pesel(createEmployeeCommand.getPesel())
                .height(createEmployeeCommand.getHeight())
                .weight(createEmployeeCommand.getWeight())
                .email(createEmployeeCommand.getEmail())
                .hireDate(createEmployeeCommand.getHireDate())
                .position(createEmployeeCommand.getPosition())
                .salary(createEmployeeCommand.getSalary())
                .build();
    }
}