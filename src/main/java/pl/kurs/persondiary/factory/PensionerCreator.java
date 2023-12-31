package pl.kurs.persondiary.factory;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.CreatePensionerCommand;
import pl.kurs.persondiary.dto.fulldto.FullPensionerDto;
import pl.kurs.persondiary.dto.fulldto.IFullPersonDto;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.dto.simpledto.SimplePensionerDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.Person;

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
        CreatePensionerCommand createPensionerCommand = new CreatePensionerCommand(getStringParameter("firstName", parameters)
                , getStringParameter("lastName", parameters)
                , getStringParameter("pesel", parameters)
                , getDoubleParameter("height", parameters)
                , getDoubleParameter("weight", parameters)
                , getStringParameter("email", parameters)
                , getBirthdateFromPesel(getStringParameter("pesel", parameters))
                , getDoubleParameter("pension", parameters)
                , getIntegerParameter("workedYears", parameters));

        commandValidator(createPensionerCommand, (org.springframework.validation.Validator) validator);
        return modelMapper.map(createPensionerCommand, Pensioner.class);
    }

    @Override
    public Person update(Person person, Map<String, Object> parameters) {
        Pensioner pensioner = (Pensioner) person;
        Optional.ofNullable(getStringParameter("firstName", parameters)).ifPresent(pensioner::setFirstName);
        Optional.ofNullable(getStringParameter("lastName", parameters)).ifPresent(pensioner::setLastName);
        Optional.ofNullable(getStringParameter("pesel", parameters)).ifPresent(x -> {
            pensioner.setPesel(x);
            pensioner.setBirthdate(getBirthdateFromPesel(x));
        });
        Optional.ofNullable(getDoubleParameter("height", parameters)).ifPresent(pensioner::setHeight);
        Optional.ofNullable(getDoubleParameter("weight", parameters)).ifPresent(pensioner::setWeight);
        Optional.ofNullable(getStringParameter("email", parameters)).ifPresent(pensioner::setEmail);
        Optional.ofNullable(getIntegerParameter("version", parameters)).ifPresent(pensioner::setVersion);
        Optional.ofNullable(getDoubleParameter("pension", parameters)).ifPresent(pensioner::setPension);
        Optional.ofNullable(getIntegerParameter("workedYears", parameters)).ifPresent(pensioner::setWorkedYears);
        return pensioner;
    }

    @Override
    public ISimplePersonDto createSimpleDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner) person;
        return modelMapper.map(pensioner, SimplePensionerDto.class);
    }

    @Override
    public IFullPersonDto createDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner) person;
        return modelMapper.map(pensioner, FullPensionerDto.class);
    }

    @Override
    public Object getEntityClass() {
        return Pensioner.class;
    }

}
