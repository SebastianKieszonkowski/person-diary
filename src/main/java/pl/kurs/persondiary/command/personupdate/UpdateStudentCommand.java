package pl.kurs.persondiary.command.personupdate;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.persondiary.command.ICreatePersonCommand;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentCommand implements ICreatePersonCommand {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
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
