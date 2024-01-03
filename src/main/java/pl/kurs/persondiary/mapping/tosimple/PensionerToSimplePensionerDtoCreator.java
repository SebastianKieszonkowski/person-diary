package pl.kurs.persondiary.mapping.tosimple;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.simpledto.SimplePensionerDto;
import pl.kurs.persondiary.models.Pensioner;

@Service
public class PensionerToSimplePensionerDtoCreator implements Converter<Pensioner, SimplePensionerDto> {

    @Override
    public SimplePensionerDto convert(MappingContext<Pensioner, SimplePensionerDto> mappingContext) {
        Pensioner source = mappingContext.getSource();
        return new SimplePensionerDto("pensioner"
                , source.getId()
                , source.getFirstName()
                , source.getLastName()
                , source.getPesel()
                , source.getHeight()
                , source.getWeight()
                , source.getEmail()
                , source.getVersion()
                , source.getPension()
                , source.getWorkedYears());
    }
}
