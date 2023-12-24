package pl.kurs.persondiary.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ExceptionResponseDto {
    private final List<String> errorMessages;
    private final String errorCode;
    private final LocalDateTime timestamp;
}
