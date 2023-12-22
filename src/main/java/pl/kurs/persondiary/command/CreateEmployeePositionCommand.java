package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import pl.kurs.persondiary.validations.PositionName;

import java.time.LocalDate;

@Getter
public class CreateEmployeePositionCommand {

    @NotBlank
    @PositionName
    private String positionName;
    @PastOrPresent
    private LocalDate startDateOnPosition;
    private LocalDate endDateOnPosition;
    @Positive
    private Double salary;
}
