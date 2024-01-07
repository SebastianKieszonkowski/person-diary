package pl.kurs.persondiary.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.ImportProgressInfo;
import pl.kurs.persondiary.repositories.ImportProgressRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ImportProgressService {

    private final Map<String, ImportProgressInfo> progressMap = new ConcurrentHashMap<>();
    private final ImportProgressRepository importProgressRepository;

    @Transactional
    public void initImport(String task) {
        ImportProgressInfo info = new ImportProgressInfo(task);
        info.setStatus("Init");
        info.setTask(task);
        progressMap.put(task, importProgressRepository.saveAndFlush(info));
    }

    public void logException(String task, Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/logs.txt", true))) {
            ImportProgressInfo info = progressMap.getOrDefault(task, new ImportProgressInfo());
            out.println("Task ID: " + task + " | Time: " + LocalDateTime.now() + " | Error: " + e.getMessage());
            info.setFailureLines(info.getFailureLines() + 1);
            progressMap.put(task, info);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void updateProgress(Long progress, String task) {
        ImportProgressInfo info = progressMap.getOrDefault(task, new ImportProgressInfo());
        info.setProcessedLines(progress);
        progressMap.put(task, info);
    }

    public void startImport(String task) {
        ImportProgressInfo info = progressMap.getOrDefault(task, new ImportProgressInfo());
        info.setStartTime(LocalDateTime.now());
        info.setStatus("In Progress");
        progressMap.put(task, info);
        importProgressRepository.saveAndFlush(info);
    }

    public void abortedImport(String task) {
        ImportProgressInfo info = progressMap.getOrDefault(task, new ImportProgressInfo());
        info.setFinishTime(LocalDateTime.now());
        info.setStatus("Aborted");
        progressMap.put(task, info);
    }

    public void completeImport(String task) {
        ImportProgressInfo info = progressMap.getOrDefault(task, new ImportProgressInfo());
        info.setFinishTime(LocalDateTime.now());
        info.setStatus("Completed");
        progressMap.put(task, info);
    }

    public ImportProgressInfo getProgressInfo(String task) {
        if (progressMap.containsKey(task)) {
            return progressMap.get(task);
        }
        return importProgressRepository.findByTask(task).orElse(new ImportProgressInfo());
    }

    @Transactional
    public void pushImportStatusToDb(String task) {
        if (progressMap.containsKey(task)) {
            ImportProgressInfo info = progressMap.get(task);
            importProgressRepository.saveAndFlush(info);
            progressMap.remove(task);
        }
    }
}
