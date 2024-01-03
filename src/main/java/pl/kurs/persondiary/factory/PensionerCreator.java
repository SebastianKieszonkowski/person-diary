package pl.kurs.persondiary.factory;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.personcreate.CreatePensionerCommand;
import pl.kurs.persondiary.command.personupdate.UpdatePensionerCommand;
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

        UpdatePensionerCommand updatePensioner = new UpdatePensionerCommand(
                Optional.ofNullable(getStringParameter("firstName", parameters)).orElse(pensioner.getFirstName()),
                Optional.ofNullable(getStringParameter("lastName", parameters)).orElse(pensioner.getLastName()),
                Optional.ofNullable(getDoubleParameter("height", parameters)).orElse(pensioner.getHeight()),
                Optional.ofNullable(getDoubleParameter("weight", parameters)).orElse(pensioner.getWeight()),
                Optional.ofNullable(getStringParameter("email", parameters)).orElse(pensioner.getEmail()),
                Optional.ofNullable(getDoubleParameter("pension", parameters)).orElse(pensioner.getPension()),
                Optional.ofNullable(getIntegerParameter("workedYears", parameters)).orElse(pensioner.getWorkedYears()));

        commandValidator(updatePensioner, (org.springframework.validation.Validator) validator);

        pensioner.setFirstName(updatePensioner.getFirstName());
        pensioner.setLastName(updatePensioner.getLastName());
        pensioner.setHeight(updatePensioner.getHeight());
        pensioner.setWeight(updatePensioner.getWeight());
        pensioner.setEmail(updatePensioner.getEmail());
        pensioner.setPension(updatePensioner.getPension());
        pensioner.setWorkedYears(updatePensioner.getWorkedYears());

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
