package pl.kurs.persondiary.command;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePersonCommand2 implements ICreatePersonCommand{
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    //@PESEL
    private String pesel;
    @NotNull
    private Double height;
    @NotNull
    private Double weight;
    @Email
    private String email;


}
