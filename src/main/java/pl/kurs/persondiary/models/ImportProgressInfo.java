package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "imports")
public class ImportProgressInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private LocalDateTime creationTime;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String status;
    private Long processedLines;
    private Long failureLines;
    private String task;

    public ImportProgressInfo(String task) {
        this.creationTime = LocalDateTime.now();
        this.status = "Created";
        this.processedLines = 0L;
        this.failureLines = 0L;
        this.task = task;
    }
}