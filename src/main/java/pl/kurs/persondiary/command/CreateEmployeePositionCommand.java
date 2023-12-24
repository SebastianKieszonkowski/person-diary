package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.kurs.persondiary.validations.PositionName;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateEmployeePositionCommand {

    @NotBlank
    @PositionName
    private String positionName;
    @PastOrPresent
    private LocalDate startDateOnPosition;
    @DateTimeFormat
    private LocalDate endDateOnPosition;
    @Positive
    private Double salary;
}
