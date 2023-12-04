package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.Student;

@Service
public class StudentToStudentViewDtoCreator implements Converter<Student, StudentViewDto> {

    @Override
    public StudentViewDto convert(MappingContext<Student, StudentViewDto> mappingContext) {
        Student source = mappingContext.getSource();
        return StudentViewDto.builder()
                .type("student")
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .universityName(source.getUniversityName())
                .studyYear(source.getStudyYear())
                .studyField(source.getStudyField())
                .scholarship(source.getScholarship())
                .build();
    }
}
