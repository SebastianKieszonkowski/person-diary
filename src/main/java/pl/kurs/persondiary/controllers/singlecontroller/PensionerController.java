package pl.kurs.persondiary.controllers.singlecontroller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.services.entityservices.PensionerService;

import java.util.List;

@RestController
@RequestMapping(path = "/pensioners")
@Validated
@RequiredArgsConstructor
public class PensionerController {

    private final PensionerService pensionerService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity getAllPensioners() {
        List<Pensioner> studentsPage = pensionerService.getAll();
//        List<FullStudentDto> fullStudentDtoPage = studentsPage.stream()
//                .map(x -> modelMapper.map(x,FullStudentDto.class))
//                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsPage);
    }
//    @Async
//    @PostMapping("/upload-jdbc")
//    public CompletableFuture<ResponseEntity<Void>> addManyAsCsvFileJdbc(@RequestParam("file") MultipartFile file) {
//        return pensionerService.addRecordFromFile(file)
//                .thenApply(aVoid -> new ResponseEntity<>(HttpStatus.CREATED));
//    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAll() {
        pensionerService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano wszystkich emerytów"), HttpStatus.OK);
    }
}
