package pl.kurs.persondiary.factory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.PensionerViewDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator{

    private ModelMapper modelMapper;

    @Override
    public String getType() {
        return "pensioner";
    }

    @Override
    public Person create(@Valid Map<String, Object> parameters) {
        return new Pensioner(getStringParameter("firstName", parameters),
                getStringParameter("lastName", parameters),
                getStringParameter("pesel", parameters),
                getDoubleParameter("height", parameters),
                getDoubleParameter("weight", parameters),
                getStringParameter("email", parameters),
                getDoubleParameter("pension", parameters),
                getIntegerParameter("workedYears", parameters));
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, PensionerViewDto.class);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner)person;
        return modelMapper.map(pensioner, PensionerViewDto.class);
    }
}
