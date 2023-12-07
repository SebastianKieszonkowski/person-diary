package pl.kurs.persondiary.controllers.singlecontroller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.services.entityservices.PensionerService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/pensioners")
@Validated
@RequiredArgsConstructor
public class PensionerController {

    private final PensionerService pensionerService;
    private final ModelMapper modelMapper;

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseEntity addManyAsCsvFile(@RequestParam("file") MultipartFile file){
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .map(args -> new Pensioner(null, args[1],args[2],args[3],Double.parseDouble(args[4]),Double.parseDouble(args[5]),
                        args[6],Double.parseDouble(args[7]),Integer.parseInt(args[8])))
                .forEach(pensionerService::add);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/upload-jdbc")
    public ResponseEntity addManyAsCsvFileJdbc(@RequestParam("file")MultipartFile file){
        pensionerService.addRecordFromFile(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<StatusDto> deleteAll(){
        pensionerService.deleteAll();
        return new ResponseEntity<>(new StatusDto("Skasowano wszystkich studentów"), HttpStatus.OK);
    }
}
