package pl.kurs.persondiary.controllers.singlecontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.command.CreateEmployeeCommand;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.dto.FullEmployeeDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.services.entityservices.EmployeePositionService;
import pl.kurs.persondiary.services.entityservices.EmployeeService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/employees")
@Validated
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeePositionService employeePositionService;
    private final ModelMapper modelMapper;

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseEntity addManyAsCsvFile(@RequestParam("file") MultipartFile file) {
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .map(args -> new Employee(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], 0, LocalDate.parse(args[7]), args[8], Double.parseDouble(args[9])))
                .forEach(employeeService::add);
        //.map(employee -> new EmployeePosition(employee.getPosition(),employee.getHireDate(),null, employee.getSalary(), employee))
        //.forEach(employeePositionService::add);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity createEmployee(@RequestBody @Valid CreateEmployeeCommand createEmployCommand) {
        Employee employeeCreated = employeeService.add(modelMapper.map(createEmployCommand, Employee.class));
        EmployeePosition employeePosition = new EmployeePosition(createEmployCommand.getPosition(), createEmployCommand.getHireDate(),
                null, createEmployCommand.getSalary(), employeeCreated);
        EmployeePosition employeePositionCreated = employeePositionService.add(employeePosition);
        FullEmployeeDto fullEmployeeDto = modelMapper.map(employeeCreated, FullEmployeeDto.class);
        return new ResponseEntity<>(fullEmployeeDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDto> deleteEmployeeById(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(new StatusDto("Skasowano pracownika o id: " + id), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAllEmployeeById() {
        employeeService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano Wszystkich pracowników"), HttpStatus.OK);
    }

    // http://localhost:8080/employee?size=1&page=0
    @GetMapping
    public ResponseEntity getAllEmployee(@PageableDefault Pageable pageable) {
        List<Employee> employeesPage = employeeService.getAll();
        List<FullEmployeeDto> fullEmployeesDtoPage = employeesPage.stream()
                .map(x -> modelMapper.map(x, FullEmployeeDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fullEmployeesDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.get(id);
        FullEmployeeDto fullEmployeeDto = modelMapper.map(employee, FullEmployeeDto.class);
        return ResponseEntity.ok(fullEmployeeDto);
        //LazyInitializationException - rozważyć dodanie do exception handlera
    }

}
