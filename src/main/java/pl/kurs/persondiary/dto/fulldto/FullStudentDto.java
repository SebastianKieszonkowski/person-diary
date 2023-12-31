package pl.kurs.persondiary.dto.fulldto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FullStudentDto implements IFullPersonDto {
    private String type;
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private LocalDate birthdate;
    private Integer version;
    private String universityName;
    private Integer studyYear;
    private String studyField;
    private Double scholarship;
}
