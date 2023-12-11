package pl.kurs.persondiary.mapping.viewdto;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.models.Employee;

@Service
public class EmployeeToEmployeeViewDtoCreator implements Converter<Employee, EmployeeViewDto> {

    @Override
    public EmployeeViewDto convert(MappingContext<Employee, EmployeeViewDto> mappingContext) {
        Employee source = mappingContext.getSource();
        return EmployeeViewDto.builder()
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
