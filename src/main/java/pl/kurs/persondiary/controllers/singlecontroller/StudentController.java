package pl.kurs.persondiary.controllers.singlecontroller;

import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.dto.singledto.FullEmployeeDto;
import pl.kurs.persondiary.dto.singledto.FullStudentDto;
import pl.kurs.persondiary.models.Employee;
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
public class StudentController {
    private final StudentService studentService;
    private final ModelMapper modelMapper;

    public StudentController(StudentService studentService, ModelMapper modelMapper) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<FullStudentDto>> getAllStudents(@PageableDefault Pageable pageable) {
        Page<Student> studentsPage = studentService.findAllPageable(pageable);
        List<FullStudentDto> fullStudentDtoPage = studentsPage.stream()
                .map(x -> modelMapper.map(x,FullStudentDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fullStudentDtoPage);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseEntity addManyAsCsvFile(@RequestParam("file")MultipartFile file){
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .map(args -> new Student(null,args[0],args[1],args[2],Double.parseDouble(args[3]),Double.parseDouble(args[4]),
                        args[5],args[6],Integer.parseInt(args[7]),args[8],Double.parseDouble(args[9])))
                .forEach(studentService::add);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
