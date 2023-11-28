package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.models.Employee;

@Service
public class EmployeeToFullEmployeeDtp implements Converter<Employee, FullEmployeeDto> {
    @Override
    public FullEmployeeDto convert(MappingContext<Employee, FullEmployeeDto> mappingContext) {
        Employee source = mappingContext.getSource();
        return FullEmployeeDto.builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .position(source.getPosition())
                .hireDate(source.getHireDate())
                .salary(source.getSalary())
                .positionHistory(source.getEmployeePositions().size())
                .build();
    }
}
