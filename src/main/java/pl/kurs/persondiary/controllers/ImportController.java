package pl.kurs.persondiary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.dto.StatusDto;
import pl.kurs.persondiary.models.ImportProgressInfo;
import pl.kurs.persondiary.services.ImportService;
import pl.kurs.persondiary.services.ImportProgressService;

@RestController
@RequestMapping(path = "/import")
@RequiredArgsConstructor
@Validated
public class ImportController {

    private final ImportService importService;
    private final ImportProgressService importProgressService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_IMPORTER')")
    public ResponseEntity<StatusDto> importCsvFile(@RequestParam("file") MultipartFile file) {
        String taskId = importService.importFile(file);
        return new ResponseEntity<>(new StatusDto("Data import has started. Task number: " + taskId), HttpStatus.OK);
    }

    @GetMapping("/status/{taskId}")
    public ResponseEntity<ImportProgressInfo> getProgress(@PathVariable String taskId) {
        ImportProgressInfo progress = importProgressService.getProgressInfo(taskId);
        return ResponseEntity.ok(progress);
    }
}
