package pl.kurs.persondiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.FindPersonQuery;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.services.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/persons")
@RequiredArgsConstructor
@Validated
public class PersonController {
    private final PersonService personService;
    private final PersonFactory personFactory;

    //    private final PersonService personService;

    @GetMapping()
    public ResponseEntity getPersons(FindPersonQuery query, @PageableDefault Pageable pageable) {
        List<PersonView> personViewList = personService.findPersonByParameters(query, pageable);
        List<IPersonDto> personDtoList = personViewList.stream().map(personFactory::createDtoFromView)
                .collect(Collectors.toList());
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createShape(@RequestBody CreatePersonCommand createPersonCommand) {
        //docelowo przenieść to do serwisu
        Person person = personFactory.create(createPersonCommand);
        person = personService.savePerson(person);
        IPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{pesel}")
    public ResponseEntity editPerson(@PathVariable String pesel, @RequestBody CreatePersonCommand updatePersonCommand){
        PersonView person = personService.findByPesel(pesel);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

//    @PostConstruct
//    public void initTestData(){
//        personService.add(new Employee(null, "Stefan", "Kopyto", "91081504852", 1.9, 100.1, "stefan.kopyto@gmail.com", LocalDate.now(), "PM", 21_359.85));
//        personService.add(new Pensioner(null, "Alicja", "Gordon", "51081504826", 1.7, 53.1, "alicja.gordon@gmail.com", 6_459.85, 54));
//        personService.add(new Pensioner(null, "Adam", "Konkol", "55081504887", 1.82, 78.1, "adam.konkol@gmail.com", 7_459.85, 49));
//        personService.add(new Employee(null, "Karol", "Polak", "97081504858", 1.6, 65.1, "karol.polak@gmail.com", LocalDate.now(), "Developer", 11_399.85));
//        personService.add(new Student(null, "Piotr", "Gad", "99081504887", 1.85, 72.1, "piotr.gad@gmail.com", "PW", 5, "Mechatronika", 2_000.0));
//    }

}
