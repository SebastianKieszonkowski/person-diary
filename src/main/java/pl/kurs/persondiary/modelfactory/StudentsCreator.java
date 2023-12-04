package pl.kurs.persondiary.modelfactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.kurs.persondiary.command.ICreatePersonCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.creators.StudentServiceCreator;

@Service
@RequiredArgsConstructor
@Validated
public class StudentsCreator implements PersonCreator {
    private final ModelMapper modelMapper;
    private final StudentServiceCreator studentServiceCreator;

    @Override
    public String getType() {
        return "STUDENT";
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, StudentViewDto.class);
    }

    @Override
    public Person createPerson(JsonNode personJsonNode) {
        CreateStudentCommand createStudentCommand  = studentServiceCreator.prepareStudentCommand(personJsonNode);
        return studentServiceCreator.createStudent(createStudentCommand);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Student student = (Student)person;
        return modelMapper.map(student, StudentViewDto.class);
    }
}

