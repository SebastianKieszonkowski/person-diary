package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

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
                .pesel(source.getPesel())
                .position(source.getPosition())
                .hireDate(source.getHireDate())
                .salary(source.getSalary())
                .positionHistory(Optional.ofNullable(source.getEmployeePositions()).orElse(Set.of()).size())
                .build();
    }
}
