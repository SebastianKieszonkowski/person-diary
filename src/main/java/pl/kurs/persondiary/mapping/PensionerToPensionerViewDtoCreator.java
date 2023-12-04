package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.viewdto.PensionerViewDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PensionerToPensionerViewDtoCreator implements Converter<Pensioner, PensionerViewDto> {
    @Override
    public PensionerViewDto convert(MappingContext<Pensioner, PensionerViewDto> mappingContext) {
        Pensioner source = mappingContext.getSource();
        return PensionerViewDto.builder()
                .type("pensioner")
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .pension(source.getPension())
                .workedYears(source.getWorkedYears())
                .build();
    }
}
