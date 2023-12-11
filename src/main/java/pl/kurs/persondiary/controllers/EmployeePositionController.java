package pl.kurs.persondiary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.CreateEmployeePositionCommand;
import pl.kurs.persondiary.command.UpdateEmployeePositionCommand;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.exeptions.IncorrectDateRangeException;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.services.ProgressService;
import pl.kurs.persondiary.services.entityservices.EmployeePositionService;
import pl.kurs.persondiary.services.entityservices.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/positions")
@Validated
public class EmployeePositionController {
    private final EmployeePositionService employeePositionService;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final ProgressService progressService;

    @PostMapping
    public ResponseEntity<FullEmployeePositionDto> createEmployeePosition(@RequestBody @Valid CreateEmployeePositionCommand createEmployeePositionCommand) {
        if(!employeePositionService.checkDates(createEmployeePositionCommand.getStartDateOnPosition(),createEmployeePositionCommand.getEndDateOnPosition()
                ,createEmployeePositionCommand.getEmployeeId()).isEmpty()) throw new IncorrectDateRangeException("Podany okres pracy pokrywa się z juz istniejącymi!!!");
        EmployeePosition employeePosition = modelMapper.map(createEmployeePositionCommand, EmployeePosition.class);
        Employee employee = employeeService.get(createEmployeePositionCommand.getEmployeeId());
        employeePosition.setEmployee(employee);
        EmployeePosition employeePositionCreated = employeePositionService.add(employeePosition);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePositionCreated, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<FullEmployeePositionDto> editEmployeePosition(@PathVariable Long id, @RequestBody @Valid UpdateEmployeePositionCommand updateEmployeePositionCommand){
        EmployeePosition employeePosition = employeePositionService.findById(id).orElseThrow();
        Optional.ofNullable(updateEmployeePositionCommand.getPositionName()).ifPresent(employeePosition::setPositionName);
        Optional.ofNullable(updateEmployeePositionCommand.getStartDateOnPosition()).ifPresent(employeePosition::setStartDateOnPosition);
        Optional.ofNullable(updateEmployeePositionCommand.getEndDateOnPosition()).ifPresent(employeePosition::setEndDateOnPosition);
        Optional.ofNullable(updateEmployeePositionCommand.getSalary()).ifPresent(employeePosition::setSalary);
        Optional.ofNullable(updateEmployeePositionCommand.getEmployeeId()).ifPresent(x -> {
            Employee employee = employeeService.get(updateEmployeePositionCommand.getEmployeeId());
            Optional.ofNullable(employee).ifPresent(employeePosition::setEmployee);
        });
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePosition, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDto> deleteEmployeePositionById(@PathVariable("id") Long id) {
        employeePositionService.delete(id);
        return new ResponseEntity<>(new StatusDto("Skasowano rekord stanowiska o id: " + id), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAllEmployeePositionById() {
        employeePositionService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano wszystkie rekord stanowiska o id: "), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FullEmployeePositionDto>> getAllEmployeesPosition() {
        List<EmployeePosition> employeePositions = employeePositionService.getAll();
        List<FullEmployeePositionDto> fullEmployeesPositionsDto = employeePositions.stream()
                .map(x -> modelMapper.map(x,FullEmployeePositionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fullEmployeesPositionsDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FullEmployeePositionDto> getEmployeePositionById(@PathVariable("id") Long id) {
        EmployeePosition employeePosition = employeePositionService.findById(id).orElseThrow();
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePosition, FullEmployeePositionDto.class);
        return ResponseEntity.ok(fullEmployeePositionDto);
    }
}
