package pl.kurs.persondiary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.UpdatePersonCommand;
import pl.kurs.persondiary.dto.fulldto.IFullPersonDto;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.services.PersonService;

import java.util.Map;

@RestController
@RequestMapping(path = "/persons")
@RequiredArgsConstructor
@Validated
public class PersonController {

    private final PersonService personService;
    private final PersonFactory personFactory;

    @GetMapping()
    public ResponseEntity getPersons(@RequestParam Map<String, String> query, @PageableDefault Pageable pageable) {
        Page<ISimplePersonDto> persons = personService.findPersonByParameters(query, pageable);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<IFullPersonDto> createPerson(@RequestBody @Valid CreatePersonCommand createPersonCommand) {
        Person person = personFactory.create(createPersonCommand);
        person = personService.savePerson(person);
        IFullPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{pesel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<IFullPersonDto> editPerson(@PathVariable String pesel, @RequestBody @Valid UpdatePersonCommand updatePersonCommand) {
        Person personToUpdate = personService.getPersonByTypeAndPesel(pesel, updatePersonCommand.getType());
        personToUpdate = personFactory.update(personToUpdate, updatePersonCommand);
        Person person = personService.updatePerson(personToUpdate);
        IFullPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }
}
