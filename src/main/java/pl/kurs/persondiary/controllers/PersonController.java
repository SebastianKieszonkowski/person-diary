package pl.kurs.persondiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.FindPersonQuery;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.ProgressInfo;
import pl.kurs.persondiary.services.PersonService;
import pl.kurs.persondiary.services.ProgressService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@RequestMapping(path = "/persons")
@RequiredArgsConstructor
@Validated
public class PersonController {

    //serwisy zwracają objekty domenowe
    private final PersonService personService;
    private final PersonFactory personFactory;
    private final ProgressService progressService;

    @GetMapping()
    public ResponseEntity getPersons(FindPersonQuery query, @PageableDefault Pageable pageable) {
        List<PersonView> personViewList = personService.findPersonByParameters(query, pageable);
        List<IPersonDto> personDtoList = personViewList.stream().map(personFactory::createDtoFromView)
                .collect(Collectors.toList());
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity createPerson(@RequestBody CreatePersonCommand createPersonCommand) {
        Person person = personFactory.create(createPersonCommand);
        person = personService.savePerson(person);
        IPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{pesel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity editPerson(@PathVariable String pesel, @RequestBody CreatePersonCommand updatePersonCommand) {
        //tu przerobić żeby person service przyjmował wyszukanego persona
        Person person = personService.updatePerson(pesel, updatePersonCommand);
        IPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @Async
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_IMPORTER')")
    public CompletableFuture<ResponseEntity<Void>> addManyAsCsvFile(@RequestParam("file") MultipartFile file) {
        String taskId = UUID.randomUUID().toString();
        System.out.println(taskId);
        progressService.startProgress(taskId);
        AtomicLong counter = new AtomicLong(0);

        return CompletableFuture.runAsync(() -> {
            progressService.startImport(taskId);
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                lines.forEach(line -> personService.importPerson(line, counter, taskId));
                progressService.completeImport(taskId);
            } catch (IOException e) {
                throw new RuntimeException("Error processing the file", e);
            }
        }).thenApply(unused -> new ResponseEntity<Void>(HttpStatus.CREATED));
    }

    @GetMapping("/importCsv/{taskId}")
    public ResponseEntity getProgress(@PathVariable String taskId) {
        ProgressInfo progress = progressService.getProgressInfo(taskId);
        return ResponseEntity.ok(progress);
    }
}
