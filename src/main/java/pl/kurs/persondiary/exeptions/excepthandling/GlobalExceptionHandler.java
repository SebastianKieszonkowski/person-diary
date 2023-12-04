package pl.kurs.persondiary.exeptions.excepthandling;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.persondiary.exeptions.ExceptionResponseDto;
import pl.kurs.persondiary.exeptions.MissingIdException;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseDto(List.of(e.getMessage()), "BAD_REQUEST", LocalDateTime.now())
        );
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponseDto(List.of(e.getMessage()), "NOT_FOUND", LocalDateTime.now())
        );
    }

    @ExceptionHandler({MissingIdException.class})
    public ResponseEntity<ExceptionResponseDto> handleMissingIdException(MissingIdException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseDto(List.of(e.getMessage()), "BAD_REQUEST", LocalDateTime.now())
        );
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ExceptionResponseDto> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseDto(List.of(e.getMessage()), "BAD_REQUEST", LocalDateTime.now())
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseDto(getMessagesListFromMethodArgumentNotValidException(e), "BAD_REQUEST", LocalDateTime.now())
        );
    }


    private List<String> getMessagesListFromMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getFieldErrors()
                .stream()
                .map(fe -> "Field: " + fe.getField() + " / rejected value: '" + fe.getRejectedValue() + "' / message: " + fe.getDefaultMessage())
                .collect(Collectors.toList());
    }

}
