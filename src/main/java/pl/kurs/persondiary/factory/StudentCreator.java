package pl.kurs.persondiary.factory;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.personcreate.CreateStudentCommand;
import pl.kurs.persondiary.command.personupdate.UpdateStudentCommand;
import pl.kurs.persondiary.dto.fulldto.FullStudentDto;
import pl.kurs.persondiary.dto.fulldto.IFullPersonDto;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.dto.simpledto.SimpleStudentDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.Student;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final Validator validator;

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public Person create(@Valid Map<String, Object> parameters) {
        CreateStudentCommand createStudentCommand = new CreateStudentCommand(getStringParameter("firstName", parameters)
                , getStringParameter("lastName", parameters)
                , getStringParameter("pesel", parameters)
                , getDoubleParameter("height", parameters)
                , getDoubleParameter("weight", parameters)
                , getStringParameter("email", parameters)
                , getBirthdateFromPesel(getStringParameter("pesel", parameters))
                , getStringParameter("universityName", parameters)
                , getIntegerParameter("studyYear", parameters)
                , getStringParameter("studyField", parameters)
                , getDoubleParameter("scholarship", parameters));

        commandValidator(createStudentCommand, (org.springframework.validation.Validator) validator);
        return modelMapper.map(createStudentCommand, Student.class);
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Student student = (Student) person;

        UpdateStudentCommand updateStudent = new UpdateStudentCommand(
                Optional.ofNullable(getStringParameter("firstName", parameters)).orElse(student.getFirstName()),
                Optional.ofNullable(getStringParameter("lastName", parameters)).orElse(student.getLastName()),
                Optional.ofNullable(getDoubleParameter("height", parameters)).orElse(student.getHeight()),
                Optional.ofNullable(getDoubleParameter("weight", parameters)).orElse(student.getWeight()),
                Optional.ofNullable(getStringParameter("email", parameters)).orElse(student.getEmail()),
                Optional.ofNullable(getStringParameter("universityName", parameters)).orElse(student.getUniversityName()),
                Optional.ofNullable(getIntegerParameter("studyYear", parameters)).orElse(student.getStudyYear()),
                Optional.ofNullable(getStringParameter("studyField", parameters)).orElse(student.getStudyField()),
                Optional.ofNullable(getDoubleParameter("scholarship", parameters)).orElse(student.getScholarship()));

        commandValidator(updateStudent, (org.springframework.validation.Validator) validator);

        student.setFirstName(updateStudent.getFirstName());
        student.setLastName(updateStudent.getLastName());
        student.setHeight(updateStudent.getHeight());
        student.setWeight(updateStudent.getWeight());
        student.setEmail(updateStudent.getEmail());
        student.setUniversityName(updateStudent.getUniversityName());
        student.setStudyYear(updateStudent.getStudyYear());
        student.setStudyField(updateStudent.getStudyField());
        student.setScholarship(updateStudent.getScholarship());

        return student;
    }

    @Override
    public ISimplePersonDto createSimpleDtoFromPerson(Person person) {
        Student student = (Student) person;
        return modelMapper.map(student, SimpleStudentDto.class);
    }

    @Override
    public IFullPersonDto createDtoFromPerson(Person person) {
        Student student = (Student) person;
        return modelMapper.map(student, FullStudentDto.class);
    }

    @Override
    public Object getEntityClass() {
        return Student.class;
    }
}
