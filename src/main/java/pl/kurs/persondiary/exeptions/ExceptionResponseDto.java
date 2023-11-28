package pl.kurs.persondiary.exeptions;

import java.time.LocalDateTime;
import java.util.List;

public class ExceptionResponseDto {

    private List<String> errorMessages;
    private String errorCode;
    private LocalDateTime timestamp;

    public ExceptionResponseDto(List<String> errorMessages, String errorCode, LocalDateTime timestamp) {
        this.errorMessages = errorMessages;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
