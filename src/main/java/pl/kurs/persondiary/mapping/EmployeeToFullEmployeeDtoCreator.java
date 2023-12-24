package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.FullEmployeeDto;
import pl.kurs.persondiary.models.Employee;

@Service
public class EmployeeToFullEmployeeDtoCreator implements Converter<Employee, FullEmployeeDto> {

    @Override
    public FullEmployeeDto convert(MappingContext<Employee, FullEmployeeDto> mappingContext) {
        Employee source = mappingContext.getSource();
        return FullEmployeeDto.builder()
                .type("employee")
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version((source.getVersion()))
                .hireDate(source.getHireDate())
                .position(source.getPosition())
                .salary(source.getSalary())
                .build();
    }
}
