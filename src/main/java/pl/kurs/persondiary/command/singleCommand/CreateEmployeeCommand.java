package pl.kurs.persondiary.command.singleCommand;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.persondiary.command.ICreatePersonCommand;

import java.time.LocalDate;

@Getter
public class CreateEmployeeCommand implements ICreatePersonCommand {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @PESEL
    private String pesel;
    @NotNull
    private Double height;
    @NotNull
    private Double weight;
    @Email
    private String email;
    //@Past
    private LocalDate hireDate;
    @NotBlank
    private String position;
    @NotNull
    private Double salary;

}
