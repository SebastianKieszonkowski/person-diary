package pl.kurs.persondiary.command;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.validations.Pesel;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreatePensionerCommand implements ICreatePersonCommand {

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
    @Past
    private LocalDate birthdate;
    @Positive
    private Double pension;
    @PositiveOrZero
    private Integer workedYears;
}
