package pl.kurs.persondiary.controllers.singlecontroller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.services.singleservice.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/employee")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmployeeController(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<FullEmployeeDto> createEmployee(@RequestBody @Valid CreateEmployeeCommand createEmployCommand) {
        Employee employee = employeeService.add(modelMapper.map(createEmployCommand, Employee.class));
        FullEmployeeDto fullEmployeeDto = modelMapper.map(employee, FullEmployeeDto.class);
        return new ResponseEntity<>(fullEmployeeDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDto> deleteEmployeeById(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(new StatusDto("Skasowano pracownika o id: " + id), HttpStatus.OK);
    }

    // http://localhost:8080/employee?size=1&page=0
    @GetMapping
    public ResponseEntity<List<FullEmployeeDto>> getAllEmployee(@PageableDefault Pageable pageable) {
        Page<Employee> employeesPage = employeeService.findAllPageable(pageable);
        List<FullEmployeeDto> fullEmployeesDtoPage = employeesPage.stream()
                .map(x -> modelMapper.map(x,FullEmployeeDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fullEmployeesDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FullEmployeeDto> getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.get(id);
        FullEmployeeDto fullEmployeeDto = modelMapper.map(employee, FullEmployeeDto.class);
        return ResponseEntity.ok(fullEmployeeDto);
    }

    //todo
    //wczytywanie do bazy danych
    //Dla employee dodać usuwanie człowieka z historią pozycji
    //Dla stanowisk dodać sprawdzanie czy dodane stanowisko ma odpowiednie daty
    //wyszukiwanie po pesel
    //wyszukiwanie po latach
    //wyszukiwanie po pułci itp.
    //testy z bazą h2
}
