package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateEmployeePositionCommand {

    private Long id;
    private String positionName;
    @PastOrPresent
    private LocalDate startDateOnPosition;
    private LocalDate endDateOnPosition;
    private Double salary;
    private Long employeeId;
}
