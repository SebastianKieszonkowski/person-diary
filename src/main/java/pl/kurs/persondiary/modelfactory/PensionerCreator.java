package pl.kurs.persondiary.modelfactory;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.ICreatePersonCommand;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.dto.viewdto.PensionerViewDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.creators.PensionerServiceCreator;
import pl.kurs.persondiary.services.creators.StudentServiceCreator;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator {
    private final ModelMapper modelMapper;
    private final PensionerServiceCreator pensionerServiceCreator;

    @Override
    public String getType() {
        return "PENSIONER";
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, PensionerViewDto.class);
    }

    @Override
    public Person createPerson(JsonNode personJsonNode) {
        CreatePensionerCommand createPensionerCommand = pensionerServiceCreator.preparePensionerCommand(personJsonNode);
        return pensionerServiceCreator.createPensioner(createPensionerCommand);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Pensioner pensioner = (Pensioner)person;
        return modelMapper.map(pensioner, PensionerViewDto.class);
    }
}
