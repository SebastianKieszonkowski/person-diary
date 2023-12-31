package pl.kurs.persondiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.exeptions.ImportConcurrencyException;
import pl.kurs.persondiary.models.ProgressInfo;
import pl.kurs.persondiary.services.PersonService;
import pl.kurs.persondiary.services.ProgressService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/import")
@RequiredArgsConstructor
@Validated
public class ImportController {

    private final PersonService personService;
    private final ProgressService progressService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_IMPORTER')")
    public ResponseEntity<StatusDto> importCsvFile(@RequestParam("file") MultipartFile file) {
        String taskId = UUID.randomUUID().toString();
        progressService.startProgress(taskId);
        if (!personService.getIsImportInProgress().get()) {
            personService.processFileAsync(file, taskId);
        } else {
            progressService.abortedImport(taskId);
            throw new ImportConcurrencyException("Cannot start import because another one is in progress, please try again later!");
        }
        return new ResponseEntity<>(new StatusDto("Data import has started. Task number: " + taskId), HttpStatus.OK);
    }

    @GetMapping("/status/{taskId}")
    public ResponseEntity<ProgressInfo> getProgress(@PathVariable String taskId) {
        ProgressInfo progress = progressService.getProgressInfo(taskId);
        return ResponseEntity.ok(progress);
    }
}
