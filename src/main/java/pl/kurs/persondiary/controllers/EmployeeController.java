package pl.kurs.persondiary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.CreateEmployeePositionCommand;
import pl.kurs.persondiary.command.UpdateEmployeePositionCommand;
import pl.kurs.persondiary.dto.FullEmployeePositionDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.services.personservices.EmployeeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @GetMapping(path = "/{pesel}/position")
    public ResponseEntity<List<FullEmployeePositionDto>> getAllEmployeesPosition(@PathVariable String pesel) {
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<FullEmployeePositionDto> editEmployeePosition(@PathVariable String pesel, @PathVariable Long id, @RequestBody UpdateEmployeePositionCommand updateEmployeePositionCommand) {
        Employee employee = employeeService.findByPesel(pesel);
        EmployeePosition employeePositionToUpdate = modelMapper.map(updateEmployeePositionCommand, EmployeePosition.class);
        employeePositionToUpdate.setEmployee(employee);
        employeePositionToUpdate.setId(id);
        EmployeePosition employeePositionUpdated = employeeService.updatePosition(employeePositionToUpdate);
        FullEmployeePositionDto fullEmployeePositionDto = modelMapper.map(employeePositionUpdated, FullEmployeePositionDto.class);
        return new ResponseEntity<>(fullEmployeePositionDto, HttpStatus.OK);
    }
}
