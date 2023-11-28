package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.models.EmployeePosition;

@Service
public class EmployeePositionToFullEmployeePositionDtoConverter implements Converter<EmployeePosition, FullEmployeePositionDto> {

    @Override
    public FullEmployeePositionDto convert(MappingContext<EmployeePosition, FullEmployeePositionDto> mappingContext) {
        EmployeePosition source = mappingContext.getSource();
        return FullEmployeePositionDto.builder()
                .id(source.getId())
                .positionName(source.getPositionName())
                .startDateOnPosition(source.getStartDateOnPosition())
                .endDateOnPosition(source.getEndDateOnPosition())
                .salary(source.getSalary())
                .employeeId(source.getEmployee().getId())
                .build();
    }
}
