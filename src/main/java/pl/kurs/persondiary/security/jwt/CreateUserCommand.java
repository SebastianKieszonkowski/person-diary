package pl.kurs.persondiary.security.jwt;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateUserCommand {
    private String username;
    private String password;
    private List<String> roles;
}
