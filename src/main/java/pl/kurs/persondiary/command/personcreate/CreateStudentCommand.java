package pl.kurs.persondiary.command.personcreate;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.command.ICreatePersonCommand;
import pl.kurs.persondiary.validations.Pesel;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateStudentCommand implements ICreatePersonCommand {

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
    @NotBlank
    private String universityName;
    @Min(1)
    @Max(5)
    private Integer studyYear;
    @NotBlank
    private String studyField;
    @Positive
    private Double scholarship;
}
