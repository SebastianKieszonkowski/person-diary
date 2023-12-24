package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.ProgressInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProgressService {
    private final Map<String, ProgressInfo> progressMap = new ConcurrentHashMap<>();
    private File logFile;

    public void startProgress(String taskId) {
        logFile = new File("src/importlogs", "logs.txt");
        ProgressInfo info = new ProgressInfo();
        info.setStatus("Init");
        progressMap.put(taskId, info);
    }

    public void logException(String taskId, Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/logs.txt", true))) {
            ProgressInfo info = progressMap.getOrDefault(taskId, new ProgressInfo());
            out.println("Task ID: " + taskId + " | Time: " + LocalDateTime.now() + " | Error: " + e.getMessage());
            info.setFailureLines(info.getFailureLines() + 1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void updateProgress(String taskId, Long progress) {
        ProgressInfo info = progressMap.getOrDefault(taskId, new ProgressInfo());
        info.setProcessedLines(progress);
        progressMap.put(taskId, info);
    }

    public void startImport(String taskId) {
        ProgressInfo info = progressMap.getOrDefault(taskId, new ProgressInfo());
        info.setStartTime(LocalDateTime.now());
        info.setStatus("In Progress");
        progressMap.put(taskId, info);
    }

    public void abortedImport(String taskId) {
        ProgressInfo info = progressMap.getOrDefault(taskId, new ProgressInfo());
        info.setStatus("Aborted");
        progressMap.put(taskId, info);
    }

    public void completeImport(String taskId) {
        ProgressInfo info = progressMap.getOrDefault(taskId, new ProgressInfo());
        info.setStatus("Completed");
        progressMap.put(taskId, info);
    }

    public ProgressInfo getProgressInfo(String taskId) {
        return progressMap.getOrDefault(taskId, new ProgressInfo());
    }
}
