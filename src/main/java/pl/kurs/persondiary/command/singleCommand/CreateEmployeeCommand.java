package pl.kurs.persondiary.command.singleCommand;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.persondiary.command.ICreatePersonCommand;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateEmployeeCommand implements ICreatePersonCommand {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @PESEL
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
    private String position;
    @Positive
    private Double salary;

}
