package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.FullPensionerDto;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PersonViewToFullPensionerDtoCreator implements Converter<PersonView, FullPensionerDto> {
    @Override
    public FullPensionerDto convert(MappingContext<PersonView, FullPensionerDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        return FullPensionerDto.builder()
                .type(source.getType())
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version(source.getVersion())
                .pension(source.getPension())
                .workedYears(source.getWorkedYears())
                .build();
    }
}