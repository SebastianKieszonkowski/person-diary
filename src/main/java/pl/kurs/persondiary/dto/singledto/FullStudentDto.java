package pl.kurs.persondiary.dto.singledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullStudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private String universityName;
    private Integer studyYear;
    private String studyField;
    private Double scholarship;
}
