package pl.kurs.persondiary.mapping.tosimple;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.controllers.EmployeeController;
import pl.kurs.persondiary.dto.simpledto.SimpleEmployeeDto;
import pl.kurs.persondiary.models.Employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeToSimpleEmployeeDtoCreator implements Converter<Employee, SimpleEmployeeDto> {

    @Override
    public SimpleEmployeeDto convert(MappingContext<Employee, SimpleEmployeeDto> mappingContext) {
        Employee source = mappingContext.getSource();
        SimpleEmployeeDto dto = new SimpleEmployeeDto(
                "employee"
                ,source.getId()
                ,source.getFirstName()
                ,source.getLastName()
                ,source.getPesel()
                ,source.getHeight()
                ,source.getWeight()
                ,source.getEmail()
                ,source.getVersion()
                ,source.getHireDate()
                ,source.getPosition()
                ,source.getSalary()
                ,String.valueOf(source.getEmployeePositions().size()));

        dto.add(linkTo(methodOn(EmployeeController.class).getAllEmployeesPosition(source.getPesel())).withRel("employee-positions"));
        dto.add(linkTo(methodOn(EmployeeController.class).createEmployeePosition(source.getPesel(), null)).withRel("create-employee-position"));
        return dto;
    }
}
