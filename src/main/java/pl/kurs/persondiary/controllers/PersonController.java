package pl.kurs.persondiary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.command.CreateEmployeePositionCommand;
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.UpdateEmployeePositionCommand;
import pl.kurs.persondiary.command.UpdatePersonCommand;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.dto.IFullPersonDto;
import pl.kurs.persondiary.dto.ISimplePersonDto;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.exeptions.ImportConcurrencyException;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.*;
import pl.kurs.persondiary.services.PersonService;
import pl.kurs.persondiary.services.ProgressService;
import pl.kurs.persondiary.services.entityservices.EmployeeService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;


    @GetMapping()
    public ResponseEntity getPersons(FindPersonQuery query, @PageableDefault Pageable pageable) {
        List<PersonView> personViewList = personService.findPersonByParameters(query, pageable);
        List<ISimplePersonDto> personDtoList = personViewList.stream()
                .map(personFactory::createSimpleDtoFromView)
                .collect(Collectors.toList());
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity createPerson(@RequestBody @Valid CreatePersonCommand createPersonCommand) {
        Person person = personFactory.create(createPersonCommand);
        person = personService.savePerson(person);
        IFullPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{pesel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity editPerson(@PathVariable String pesel, @RequestBody @Valid UpdatePersonCommand updatePersonCommand) {
        Person personToUpdate = personService.getPersonByTypeAndPesel(pesel, updatePersonCommand.getType());
        personToUpdate = personFactory.update(personToUpdate, updatePersonCommand);
        Person person = personService.updatePerson(personToUpdate);
        IFullPersonDto personDto = personFactory.createDtoFromPerson(person);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{pesel}/position")
    public ResponseEntity getAllEmployeesPosition(@PathVariable String pesel) {
        Set<EmployeePosition> employeePositions = employeeService.findPersonByPeselWithPosition(pesel).getEmployeePositions();
        List<FullEmployeePositionDto> fullEmployeesPositionsDto = employeePositions.stream()
                .map(x -> modelMapper.map(x, FullEmployeePositionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fullEmployeesPositionsDto);
    }

    @PostMapping(path = "/{pesel}/position")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<FullEmployeePositionDto> createEmployeePosition(@PathVariable String pesel, @RequestBody @Valid CreateEmployeePositionCommand createEmployeePositionCommand) {
        Employee employee = employeeService.findByPesel(pesel);
        EmployeePosition employeePosition = modelMapper.map(createEmployeePositionCommand, EmployeePosition.class);
        employeePosition.setEmployee(employee);
        employeeService.checkPeriodPositionConflict(employeePosition);
        EmployeePosition employeePositionCreated = employeeService.addPositionToEmployee(employeePosition);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePositionCreated, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{pesel}/position/{id}")
    public ResponseEntity editEmployeePosition(@PathVariable String pesel, @PathVariable Long id, @RequestBody @Valid UpdateEmployeePositionCommand updateEmployeePositionCommand) {
        Employee employee = employeeService.findByPesel(pesel);
        EmployeePosition employeePositionToUpdate = modelMapper.map(updateEmployeePositionCommand, EmployeePosition.class);
        employeePositionToUpdate.setEmployee(employee);
        employeePositionToUpdate.setId(id);
        EmployeePosition employeePositionUpdated = employeeService.updatePosition(employeePositionToUpdate);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePositionUpdated, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_IMPORTER')")
    public ResponseEntity addManyAsCsvFile(@RequestParam("file") MultipartFile file) {
        String taskId = UUID.randomUUID().toString();
        progressService.startProgress(taskId);
        if (!personService.getIsImportInProgress().get()) {
            personService.processFileAsync(file, taskId);
        } else {
            progressService.abortedImport(taskId);
            throw new ImportConcurrencyException("Cannot start import because another one is in progress, please try again later!");
        }

        return new ResponseEntity<>(new StatusDto("Data import has started. Task number: " + taskId), HttpStatus.OK);
    }

    @GetMapping("/importCsv/{taskId}")
    public ResponseEntity getProgress(@PathVariable String taskId) {
        ProgressInfo progress = progressService.getProgressInfo(taskId);
        return ResponseEntity.ok(progress);
    }
}
