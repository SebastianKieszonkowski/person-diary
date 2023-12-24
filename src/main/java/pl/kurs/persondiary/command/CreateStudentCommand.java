package pl.kurs.persondiary.command;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.validations.Pesel;

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
