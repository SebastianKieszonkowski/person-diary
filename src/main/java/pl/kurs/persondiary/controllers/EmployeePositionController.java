package pl.kurs.persondiary.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.CreateEmployeePositionCommand;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.services.EmployeePositionService;
import pl.kurs.persondiary.services.singleservice.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/position")
@Validated
public class EmployeePositionController {
    private final EmployeePositionService employeePositionService;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmployeePositionController(EmployeePositionService positionService, EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeePositionService = positionService;
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<FullEmployeePositionDto> createEmployeePosition(@RequestBody @Valid CreateEmployeePositionCommand createEmployeePositionCommand) {
        EmployeePosition employeePosition = modelMapper.map(createEmployeePositionCommand, EmployeePosition.class);
        //employeePosition.setId(null);
        Employee employee = employeeService.get(createEmployeePositionCommand.getEmployeeId());
        employeePosition.setEmployee(employee);
        EmployeePosition employeePositionCreated = employeePositionService.add(employeePosition);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePositionCreated, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDto> deleteEmployeePositionById(@PathVariable("id") Long id) {
        employeePositionService.delete(id);
        return new ResponseEntity<>(new StatusDto("Skasowano rekord stanowiska o id: " + id), HttpStatus.OK);
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
        EmployeePosition employeePosition = employeePositionService.get(id);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePosition, FullEmployeePositionDto.class);
        return ResponseEntity.ok(fullEmployeePositionDto);
    }
}