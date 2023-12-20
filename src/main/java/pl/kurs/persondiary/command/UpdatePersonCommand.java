package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.validations.PersonType;

import java.util.Map;

@Getter
@Setter
public class UpdatePersonCommand {

    @NotBlank
    @PersonType
    private String type;
    @NotEmpty
    private Map<String, Object> parameters;

}
