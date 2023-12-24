package pl.kurs.persondiary.command;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.validations.Pesel;
import pl.kurs.persondiary.validations.PositionName;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateEmployeeCommand implements ICreatePersonCommand {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Pesel
    private String pesel;
    @Positive
    private Double height;
    @Positive
    private Double weight;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate hireDate;
    @NotBlank
    @PositionName
    private String position;
    @Positive
    private Double salary;

}
