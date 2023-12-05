package pl.kurs.persondiary.factory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeCreator implements PersonCreator{

    private final ModelMapper modelMapper;

    @Override
    public String getType() {
        return "employee";
    }

    @Override
    public Person create(@Valid Map<String, Object> parameters) {
        return new Employee(getStringParameter("firstName", parameters),
                getStringParameter("lastName", parameters),
                getStringParameter("pesel", parameters),
                getDoubleParameter("height", parameters),
                getDoubleParameter("weight", parameters),
                getStringParameter("email", parameters),
                getLocalDataParameter("hireDate", parameters),
                getStringParameter("position", parameters),
                getDoubleParameter("salary", parameters));
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, EmployeeViewDto.class);
    }

    @Override
    public IPersonDto createDtoFromPerson(Person person) {
        Employee employee = (Employee)person;
        return modelMapper.map(employee, EmployeeViewDto.class);
    }
}
