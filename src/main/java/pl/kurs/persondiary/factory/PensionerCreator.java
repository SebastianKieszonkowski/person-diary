package pl.kurs.persondiary.factory;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.CreatePensionerCommand;
import pl.kurs.persondiary.dto.FullPensionerDto;
import pl.kurs.persondiary.dto.IFullPersonDto;
import pl.kurs.persondiary.dto.ISimplePersonDto;
import pl.kurs.persondiary.dto.SimplePensionerDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final Validator validator;

    @Override
    public String getType() {
        return "pensioner";
    }

    @Override
    public Person create(Map<String, Object> parameters) {
        CreatePensionerCommand createPensionerCommand = new CreatePensionerCommand(
                getStringParameter("firstName", parameters),
                getStringParameter("lastName", parameters),
                getStringParameter("pesel", parameters),
                getDoubleParameter("height", parameters),
                getDoubleParameter("weight", parameters),
                getStringParameter("email", parameters),
                getDoubleParameter("pension", parameters),
                getIntegerParameter("workedYears", parameters));

        commandValidator(createPensionerCommand, (org.springframework.validation.Validator) validator);
        return modelMapper.map(createPensionerCommand, Pensioner.class);
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
    public IFullPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, FullPensionerDto.class);
    }

    @Override
    public ISimplePersonDto createSimpleDtoFromView(PersonView personView) {
        return modelMapper.map(personView, SimplePensionerDto.class);
    }

    @Override
    public IFullPersonDto createDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner) person;
        return modelMapper.map(pensioner, FullPensionerDto.class);
    }

    @Override
    public Person createPersonFromView(PersonView personView) {
        return modelMapper.map(personView, Pensioner.class);
    }

}
