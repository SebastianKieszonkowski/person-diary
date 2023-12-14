package pl.kurs.persondiary.factory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.Student;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentCreator implements PersonCreator {

    private final ModelMapper modelMapper;

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public Person create(@Valid Map<String, Object> parameters) {
        return new Student(getStringParameter("firstName", parameters),
                getStringParameter("lastName", parameters),
                getStringParameter("pesel", parameters),
                getDoubleParameter("height", parameters),
                getDoubleParameter("weight", parameters),
                getStringParameter("email", parameters),
                getIntegerParameter("version", parameters),
                getStringParameter("universityName", parameters),
                getIntegerParameter("studyYear", parameters),
                getStringParameter("studyField", parameters),
                getDoubleParameter("scholarship", parameters));
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Student student = (Student) person;
        Optional.ofNullable(getStringParameter("firstName", parameters)).ifPresent(student::setFirstName);
        Optional.ofNullable(getStringParameter("lastName", parameters)).ifPresent(student::setLastName);
        Optional.ofNullable(getStringParameter("pesel", parameters)).ifPresent(student::setPesel);
        Optional.ofNullable(getDoubleParameter("height", parameters)).ifPresent(student::setHeight);
        Optional.ofNullable(getDoubleParameter("weight", parameters)).ifPresent(student::setWeight);
        Optional.ofNullable(getStringParameter("email", parameters)).ifPresent(student::setEmail);
        Optional.ofNullable(getIntegerParameter("version", parameters)).ifPresent(student::setVersion);
        Optional.ofNullable(getStringParameter("universityName", parameters)).ifPresent(student::setUniversityName);
        Optional.ofNullable(getIntegerParameter("studyYear", parameters)).ifPresent(student::setStudyYear);
        Optional.ofNullable(getStringParameter("studyField", parameters)).ifPresent(student::setStudyField);
        Optional.ofNullable(getDoubleParameter("scholarship", parameters)).ifPresent(student::setScholarship);
        return student;
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, StudentViewDto.class);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Student student = (Student) person;
        return modelMapper.map(student, StudentViewDto.class);
    }

}
