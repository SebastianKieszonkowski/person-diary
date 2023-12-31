package pl.kurs.persondiary.security.jwt;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private UserRepository repository;
    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        User newUser = mapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(savedUser, UserDto.class));
    }
}
