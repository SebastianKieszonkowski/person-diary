package pl.kurs.persondiary.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreatePersonCommand {

    @NotBlank
    private String type;
    private Map<String, Object> parameters;

}
