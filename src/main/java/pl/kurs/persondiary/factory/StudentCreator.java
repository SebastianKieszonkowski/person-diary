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

@Service
@RequiredArgsConstructor
public class StudentCreator implements PersonCreator {

    private ModelMapper modelMapper;

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
                getStringParameter("universityName", parameters),
                getIntegerParameter("studyYear", parameters),
                getStringParameter("studyField", parameters),
                getDoubleParameter("scholarship", parameters));
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, StudentViewDto.class);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Student student = (Student)person;
        return modelMapper.map(student, StudentViewDto.class);
    }
}
