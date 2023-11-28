package pl.kurs.persondiary.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.*;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;

import java.util.Optional;

@Service
public class PersonMapperService {

    final private ModelMapper modelMapper;
    final private ObjectMapper objectMapper;

    public PersonMapperService(ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public ICreatePersonCommand recognisePerson(String input) throws JsonProcessingException {

//        Map<String, Class> objectMap = new HashMap<String, Class>();
//        objectMap.put("hireDate", Employee.class);
//        objectMap.put("pension", Pensioner.class);
//        objectMap.put("universityName", Student.class);

        JsonNode personJsonNode = objectMapper.readTree(input);
        ICreatePersonCommand person = null;

        if (personJsonNode.has("hireDate")) {
            System.out.println("Employee");

            person = objectMapper.convertValue(personJsonNode, CreateEmployeeCommand.class);
        }
        if (personJsonNode.has("pension")) {
            System.out.println("Pensioner");
            person = objectMapper.convertValue(personJsonNode, CreatePensionerCommand.class);
        }
        if (personJsonNode.has("universityName")) {
            System.out.println("Student");
            person = objectMapper.convertValue(personJsonNode, CreateStudentCommand.class);
        }

        return Optional.ofNullable(person).orElseThrow();
    }
}
