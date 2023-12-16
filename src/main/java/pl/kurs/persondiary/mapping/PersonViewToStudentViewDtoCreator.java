package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PersonViewToStudentViewDtoCreator implements Converter<PersonView, StudentViewDto> {

    @Override
    public StudentViewDto convert(MappingContext<PersonView, StudentViewDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        return StudentViewDto.builder()
                .type(source.getType())
                .id(source.getSubId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version(source.getVersion())
                .universityName(source.getUniversityName())
                .studyYear(source.getStudyYear())
                .studyField(source.getStudyField())
                .scholarship(source.getScholarship())
                .build();
    }
}
