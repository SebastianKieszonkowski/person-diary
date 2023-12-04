package pl.kurs.persondiary.command.singleCommand;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.persondiary.command.ICreatePersonCommand;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateStudentCommand implements ICreatePersonCommand {

    @NotBlank
    private String type;
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
    @NotBlank
    private String universityName;
    @NotNull
    private Integer studyYear;
    @NotBlank
    private String studyField;
    @NotNull
    private Double scholarship;
}
