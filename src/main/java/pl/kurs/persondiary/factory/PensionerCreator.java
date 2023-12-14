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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator {

    private final ModelMapper modelMapper;

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
                getIntegerParameter("version", parameters),
                getDoubleParameter("pension", parameters),
                getIntegerParameter("workedYears", parameters));
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Pensioner pensioner = (Pensioner) person;
        Optional.ofNullable(getStringParameter("firstName", parameters)).ifPresent(pensioner::setFirstName);
        Optional.ofNullable(getStringParameter("lastName", parameters)).ifPresent(pensioner::setLastName);
        Optional.ofNullable(getStringParameter("pesel", parameters)).ifPresent(pensioner::setPesel);
        Optional.ofNullable(getDoubleParameter("height", parameters)).ifPresent(pensioner::setHeight);
        Optional.ofNullable(getDoubleParameter("weight", parameters)).ifPresent(pensioner::setWeight);
        Optional.ofNullable(getStringParameter("email", parameters)).ifPresent(pensioner::setEmail);
        Optional.ofNullable(getIntegerParameter("version", parameters)).ifPresent(pensioner::setVersion);
        Optional.ofNullable(getDoubleParameter("pension", parameters)).ifPresent(pensioner::setPension);
        Optional.ofNullable(getIntegerParameter("workedYears", parameters)).ifPresent(pensioner::setWorkedYears);
        return pensioner;
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, PensionerViewDto.class);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner) person;
        return modelMapper.map(pensioner, PensionerViewDto.class);
    }

}
