package pl.kurs.persondiary.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProgressInfo {

    private LocalDateTime creationTime;
    private LocalDateTime startTime;
    private String status;
    private Long processedLines;
    private Long failureLines;

    public ProgressInfo() {
        this.creationTime = LocalDateTime.now();
        this.processedLines = 0L;
        this.failureLines = 0L;
        this.status = "Created";
    }
}