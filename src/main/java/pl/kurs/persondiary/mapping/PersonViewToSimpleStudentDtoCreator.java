package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.SimpleStudentDto;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PersonViewToSimpleStudentDtoCreator implements Converter<PersonView, SimpleStudentDto> {

    @Override
    public SimpleStudentDto convert(MappingContext<PersonView, SimpleStudentDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        return SimpleStudentDto.builder()
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
