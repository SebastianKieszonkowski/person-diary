package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.controllers.PersonController;
import pl.kurs.persondiary.dto.SimpleEmployeeDto;
import pl.kurs.persondiary.models.PersonView;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonViewToSimpleEmployeeDtoCreator implements Converter<PersonView, SimpleEmployeeDto> {

    @Override
    public SimpleEmployeeDto convert(MappingContext<PersonView, SimpleEmployeeDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        SimpleEmployeeDto dto = SimpleEmployeeDto.builder()
                .type(source.getType())
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version(source.getVersion())
                .hireDate(source.getHireDate())
                .position(source.getPosition())
                .positionsHistory(source.getPositionsHistory())
                .salary(source.getSalary())
                .build();

        dto.add(linkTo(methodOn(PersonController.class).getAllEmployeesPosition(source.getPesel())).withRel("employee-positions"));
        dto.add(linkTo(methodOn(PersonController.class).createEmployeePosition(source.getPesel(), null)).withRel("create-employee-position"));
        return dto;
    }
}
