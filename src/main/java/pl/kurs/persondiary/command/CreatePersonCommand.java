package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.validations.PersonType;

import java.util.Map;

@Getter
@Setter
public class CreatePersonCommand {

    @NotBlank
    @PersonType
    private String type;
    private Map<String, Object> parameters;

}
