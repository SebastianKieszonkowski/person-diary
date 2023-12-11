package pl.kurs.persondiary.controllers.singlecontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.services.ProgressService;
import pl.kurs.persondiary.services.entityservices.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/students")
@Validated
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final ProgressService progressService;

    @PostMapping
    public ResponseEntity createStudent(@RequestBody @Valid CreateStudentCommand createStudentCommand) {
        Student studentCreated = studentService.add(modelMapper.map(createStudentCommand, Student.class));
        StudentViewDto studentViewDto = modelMapper.map(studentCreated, StudentViewDto.class);
        return new ResponseEntity<>(studentViewDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllStudents() {
        List<Student> studentsPage = studentService.getAll();
//        List<FullStudentDto> fullStudentDtoPage = studentsPage.stream()
//                .map(x -> modelMapper.map(x,FullStudentDto.class))
//                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsPage);
    }
    @Async
    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<Void>> addManyAsCsvFile(@RequestParam("file") MultipartFile file) {
        String taskId = "task2";//UUID.randomUUID().toString();
        System.out.println(taskId);
        progressService.startProgress(taskId);
        return CompletableFuture.runAsync(() -> {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                List<String> lineList = lines.collect(Collectors.toList());
                int totalLines = lineList.size();
                for (int i = 0; i < totalLines; i++) {
                    String[] args = lineList.get(i).split(",");
                    Student student = new Student(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], 0, args[7], Integer.parseInt(args[8]), args[9], Double.parseDouble(args[10]));
                    studentService.add(student);
                    // Update progress
                    //progressService.updateProgress(taskId, (i + 1) * 100 / totalLines);

                    // Sleep for 2 seconds
                    Thread.sleep(5000);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error processing the file", e);
            }
        }).thenApply(unused -> new ResponseEntity<Void>(HttpStatus.CREATED));
    }
//    @PostMapping("/upload")
//    @SneakyThrows
//    public ResponseEntity addManyAsCsvFile(@RequestParam("file") MultipartFile file) {
//        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
//        lines.map(line -> line.split(","))
//                .map(args -> new Student(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
//                        args[6], 0, args[7], Integer.parseInt(args[8]), args[9], Double.parseDouble(args[10])))
//                .forEach(studentService::add);
//        return new ResponseEntity(HttpStatus.CREATED);
//    }

    @PostMapping("/upload-jdbc")
    public ResponseEntity addManyAsCsvFileJdbc(@RequestParam("file") MultipartFile file) {
        studentService.addRecordFromFile(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAll() {
        studentService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano wszystkich studentów"), HttpStatus.OK);
    }
}
