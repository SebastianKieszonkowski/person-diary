package pl.kurs.persondiary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleStudentDto implements ISimplePersonDto {
    private String type;
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private Integer version;
    private String universityName;
    private Integer studyYear;
    private String studyField;
    private Double scholarship;
}
