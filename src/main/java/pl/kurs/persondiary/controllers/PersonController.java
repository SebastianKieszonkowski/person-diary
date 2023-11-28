package pl.kurs.persondiary.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.*;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.PersonMapperService;
import pl.kurs.persondiary.services.PersonService;

import java.util.List;

@RestController
@RequestMapping(path = "/persons")
@AllArgsConstructor
public class PersonController {
    private final PersonMapperService personMapperService;
    private final ModelMapper modelMapper;
    private final PersonService personService;

    @GetMapping()
    public ResponseEntity getAllEmploy () {
        List<Person> persons = personService.getAll();
        return ResponseEntity.ok(persons);
    }

    @GetMapping(path ="/{id}")
    public ResponseEntity getEmployeeById(@PathVariable("id") Long id) {
        Employee person = personService.getEmployeeById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity createGrade(@RequestBody String createPersonCommand) throws JsonProcessingException {
        System.out.println(createPersonCommand);
        Person person = null;
        ICreatePersonCommand recognisedPerson = personMapperService.recognisePerson(createPersonCommand);
        if (recognisedPerson.getClass().equals(CreateStudentCommand.class)) {
            person = modelMapper.map(recognisedPerson, Student.class);
        } else if (recognisedPerson.getClass().equals(CreateEmployeeCommand.class)) {
            person = modelMapper.map(recognisedPerson, Employee.class);
        } else if (recognisedPerson.getClass().equals(CreatePensionerCommand.class)) {
            person = modelMapper.map(recognisedPerson, Pensioner.class);
        }

            Person savedPerson = personService.add(person);
            return new ResponseEntity(HttpStatus.CREATED);
        }

//    @PostConstruct
//    public void initTestData(){
//        personService.add(new Employee(null, "Stefan", "Kopyto", "91081504852", 1.9, 100.1, "stefan.kopyto@gmail.com", LocalDate.now(), "PM", 21_359.85));
//        personService.add(new Pensioner(null, "Alicja", "Gordon", "51081504826", 1.7, 53.1, "alicja.gordon@gmail.com", 6_459.85, 54));
//        personService.add(new Pensioner(null, "Adam", "Konkol", "55081504887", 1.82, 78.1, "adam.konkol@gmail.com", 7_459.85, 49));
//        personService.add(new Employee(null, "Karol", "Polak", "97081504858", 1.6, 65.1, "karol.polak@gmail.com", LocalDate.now(), "Developer", 11_399.85));
//        personService.add(new Student(null, "Piotr", "Gad", "99081504887", 1.85, 72.1, "piotr.gad@gmail.com", "PW", 5, "Mechatronika", 2_000.0));
//    }


        //    @GetMapping(path = "/criteria")
//    public ResponseEntity<List<Person>> getPersonByCriteria(@RequestParam(value = "firstName", required = false) String firstName,
//                                                            @RequestParam(value = "lastName", required = false) String lastName) {
//        List<Person> persons;
//        if (firstName != null) {
//            persons = personService.getByFirstName(firstName);
//        } else if (lastName != null) {
//            persons = personService.getByLastName(lastName);
//        } else {
//            persons = personService.getAll();
//        }
//        return ResponseEntity.ok(persons);
//    }
        @GetMapping(value = "/{filter}")
        public ResponseEntity<List<Person>> filterPersons (@PathVariable("filter") String filter){
            List<Person> filteredPersons = personService.filterPersonByFilter(filter);
            return ResponseEntity.ok(filteredPersons);
        }

    }
