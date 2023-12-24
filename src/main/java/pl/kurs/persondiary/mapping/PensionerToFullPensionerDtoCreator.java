package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.FullPensionerDto;
import pl.kurs.persondiary.models.Pensioner;

@Service
public class PensionerToFullPensionerDtoCreator implements Converter<Pensioner, FullPensionerDto> {

    @Override
    public FullPensionerDto convert(MappingContext<Pensioner, FullPensionerDto> mappingContext) {
        Pensioner source = mappingContext.getSource();
        return FullPensionerDto.builder()
                .type("pensioner")
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
