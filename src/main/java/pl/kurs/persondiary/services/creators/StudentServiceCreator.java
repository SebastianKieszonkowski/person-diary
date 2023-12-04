package pl.kurs.persondiary.services.creators;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.models.Student;

@Service
@Validated
public class StudentServiceCreator {

    public CreateStudentCommand prepareStudentCommand(JsonNode personJsonNode){
        return CreateStudentCommand.builder()
                .type(personJsonNode.get("type").asText())
                .firstName(personJsonNode.get("firstName").asText())
                .lastName(personJsonNode.get("lastName").asText())
                .pesel(personJsonNode.get("pesel").asText())
                .height(personJsonNode.get("height").asDouble())
                .weight(personJsonNode.get("weight").asDouble())
                .email(personJsonNode.get("email").asText())
                .universityName(personJsonNode.get("universityName").asText())
                .studyYear(personJsonNode.get("studyYear").asInt())
                .studyField(personJsonNode.get("studyField").asText())
                .scholarship(personJsonNode.get("scholarship").asDouble())
                .build();
    }
    public Student createStudent(@Valid CreateStudentCommand createStudentCommand) {
        return Student.builder()
                .firstName(createStudentCommand.getFirstName())
                .lastName(createStudentCommand.getLastName())
                .pesel(createStudentCommand.getPesel())
                .height(createStudentCommand.getHeight())
                .weight(createStudentCommand.getWeight())
                .email(createStudentCommand.getEmail())
                .universityName(createStudentCommand.getUniversityName())
                .studyYear(createStudentCommand.getStudyYear())
                .studyField(createStudentCommand.getStudyField())
                .scholarship(createStudentCommand.getScholarship())
                .build();
    }
}