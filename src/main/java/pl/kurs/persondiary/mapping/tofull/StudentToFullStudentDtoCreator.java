package pl.kurs.persondiary.mapping.tofull;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.fulldto.FullStudentDto;
import pl.kurs.persondiary.models.Student;

@Service
public class StudentToFullStudentDtoCreator implements Converter<Student, FullStudentDto> {

    @Override
    public FullStudentDto convert(MappingContext<Student, FullStudentDto> mappingContext) {
        Student source = mappingContext.getSource();
        return FullStudentDto.builder()
                .type("student")
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version((source.getVersion()))
                .birthdate(source.getBirthdate())
                .universityName(source.getUniversityName())
                .studyField(source.getStudyField())
                .scholarship(source.getScholarship())
                .studyYear(source.getStudyYear())
                .build();
    }
}
