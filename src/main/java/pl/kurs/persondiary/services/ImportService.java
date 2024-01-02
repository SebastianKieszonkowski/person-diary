package pl.kurs.persondiary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.exeptions.ImportConcurrencyException;
import pl.kurs.persondiary.repositories.ImportProgressRepository;
import pl.kurs.persondiary.services.importcsv.ImportFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportProgressService importProgressService;
    private final ImportFactory importFactory;
    private final ImportProgressRepository importProgressRepository;

    @Transactional
    public String importFile(MultipartFile file) {
        String taskId = UUID.randomUUID().toString();
        importProgressService.startProgress(taskId);
        if (!importProgressRepository.existsByStatus("In Progress")) {
            importProgressService.startImport();
            processFileAsync(file, taskId);
        } else {
            importProgressService.abortedImport();
            throw new ImportConcurrencyException("Cannot start import because another one is in progress, please try again later!");
        }
        return taskId;
    }

    @Async
    @Transactional
    public void processFileAsync(MultipartFile file, String taskId) {
        AtomicLong counter = new AtomicLong(0);
        try {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                lines.forEach(line -> importPerson(line, counter, taskId));
                importProgressService.completeImport();
            } catch (IOException | DuplicateKeyException e) {
                throw new RuntimeException("Error processing the file", e);
            }
        } catch (Exception e) {
            importProgressService.logException(taskId, e);
            importProgressService.abortedImport();
        }
    }

    private void importPerson(String line, AtomicLong counter, String taskId) {
        String[] args = line.split(",");
        importFactory.importPerson(args);
        Long processedLines = counter.incrementAndGet();
        importProgressService.updateProgress(processedLines);
    }

}
