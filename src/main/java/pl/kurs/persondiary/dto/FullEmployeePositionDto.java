package pl.kurs.persondiary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FullEmployeePositionDto {
    private Long id;
    private String positionName;
    private LocalDate startDateOnPosition;
    private LocalDate endDateOnPosition;
    private Double salary;
    private Long employeeId;
}
