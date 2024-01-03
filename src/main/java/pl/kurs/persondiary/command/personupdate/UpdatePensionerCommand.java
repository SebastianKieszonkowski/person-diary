package pl.kurs.persondiary.command.personupdate;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.command.ICreatePersonCommand;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePensionerCommand implements ICreatePersonCommand {

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
    @Positive
    private Double pension;
    @PositiveOrZero
    private Integer workedYears;
}
