package pl.kurs.persondiary.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateEmployeePositionCommand {

    private String positionName;
    private LocalDate startDateOnPosition;
    private LocalDate endDateOnPosition;
    private Double salary;
}
