package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.FullStudentDto;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PersonViewToFullStudentDtoCreator implements Converter<PersonView, FullStudentDto> {

    @Override
    public FullStudentDto convert(MappingContext<PersonView, FullStudentDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        return FullStudentDto.builder()
                .type(source.getType())
                .id(source.getId())
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
