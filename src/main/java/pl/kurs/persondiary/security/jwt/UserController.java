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

//    @PostConstruct
//    public void init() {
//        UserRole adminRole = new UserRole("ROLE_ADMIN");
//        UserRole userRole = new UserRole("ROLE_USER");
//        UserRole importerRole = new UserRole("ROLE_IMPORTER");
//        UserRole userEmployee = new UserRole("ROLE_EMPLOYEE");
//
//        User admin = new User("AdamAdmin", passwordEncoder.encode("admin"), Set.of(adminRole));
//        User user = new User("JanekUser", passwordEncoder.encode("user"), Set.of(userRole));
//        User importer = new User("KarolImporter", passwordEncoder.encode("importer"), Set.of(importerRole));
//        User employee = new User("DarekEmployee", passwordEncoder.encode("employee"), Set.of(userEmployee));
//
//        userRepository.saveAll(List.of(admin, user, importer, employee));
//    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        User newUser = mapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(savedUser, UserDto.class));
    }


}
