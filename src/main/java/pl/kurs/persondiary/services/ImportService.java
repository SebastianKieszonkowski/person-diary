package pl.kurs.persondiary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.async.AsyncFileProcessor;
import pl.kurs.persondiary.exeptions.ImportConcurrencyException;
import pl.kurs.persondiary.repositories.ImportProgressRepository;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportProgressService importProgressService;
    private final AsyncFileProcessor asyncFileProcessor;
    private final ImportProgressRepository importProgressRepository;

    @Transactional
    public void importFile(MultipartFile file, String taskId) {
        if (!importProgressRepository.existsByStatus("In Progress")) {
            importProgressService.startImport();
            asyncFileProcessor.processFileAsync(file, taskId);
        } else {
            importProgressService.abortedImport();
            throw new ImportConcurrencyException("Cannot start import because another one is in progress, please try again later!");
        }
    }
}
