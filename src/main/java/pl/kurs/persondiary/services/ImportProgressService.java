package pl.kurs.persondiary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.ImportProgressInfo;
import pl.kurs.persondiary.repositories.ImportProgressRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImportProgressService {

    private final ImportProgressRepository importProgressRepository;
    private File logFile;
    private ImportProgressInfo importProgressInfo;

    public void startProgress(String taskId) {
        logFile = new File("src/importlogs", "logs.txt");
        importProgressInfo = new ImportProgressInfo(taskId);
        importProgressInfo = importProgressRepository.saveAndFlush(importProgressInfo);
    }

    public void logException(String taskId, Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/logs.txt", true))) {
            out.println("Task ID: " + taskId + " | Time: " + LocalDateTime.now() + " | Error: " + e.getMessage());
            importProgressInfo.setFailureLines(importProgressInfo.getFailureLines() + 1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void updateProgress(Long progress) {
        importProgressInfo.setProcessedLines(progress);
    }

    public void startImport() {
        importProgressInfo.setStartTime(LocalDateTime.now());
        importProgressInfo.setStatus("In Progress");
        importProgressRepository.saveAndFlush(importProgressInfo);
    }

    public void abortedImport() {
        importProgressInfo.setFinishTime(LocalDateTime.now());
        importProgressInfo.setStatus("Aborted");
        importProgressRepository.saveAndFlush(importProgressInfo);
    }

    public void completeImport() {
        importProgressInfo.setFinishTime(LocalDateTime.now());
        importProgressInfo.setStatus("Completed");
        importProgressRepository.saveAndFlush(importProgressInfo);
    }

    public ImportProgressInfo getProgressInfo(String taskId) {
        if (importProgressInfo != null ) {
            if(importProgressInfo.getTask().equals(taskId))
                return importProgressInfo;
        }
        return importProgressRepository.findByTask(taskId).orElse(new ImportProgressInfo());
    }
}
