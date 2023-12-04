package pl.kurs.persondiary.controllers.singlecontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.dto.singledto.FullStudentDto;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.singleservice.EmployeeService;
import pl.kurs.persondiary.services.singleservice.StudentService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/students")
@Validated
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createStudent(@RequestBody @Valid CreateStudentCommand createStudentCommand) {
        Student studentCreated = studentService.add(modelMapper.map(createStudentCommand, Student.class));
         FullStudentDto fullStudentDto = modelMapper.map(studentCreated, FullStudentDto.class);
        return new ResponseEntity<>(fullStudentDto, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity getAllStudents() {
        List<Student> studentsPage = studentService.getAll();
//        List<FullStudentDto> fullStudentDtoPage = studentsPage.stream()
//                .map(x -> modelMapper.map(x,FullStudentDto.class))
//                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsPage);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseEntity addManyAsCsvFile(@RequestParam("file")MultipartFile file){
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .map(args -> new Student(null, args[1],args[2],args[3],Double.parseDouble(args[4]),Double.parseDouble(args[5]),
                        args[6],args[7],Integer.parseInt(args[8]),args[9],Double.parseDouble(args[10])))
                .forEach(studentService::add);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/upload-jdbc")
    public ResponseEntity addManyAsCsvFileJdbc(@RequestParam("file")MultipartFile file){
        studentService.addRecordFromFile(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAll(){
        studentService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano wszystkich studentów"), HttpStatus.OK);
    }
}
