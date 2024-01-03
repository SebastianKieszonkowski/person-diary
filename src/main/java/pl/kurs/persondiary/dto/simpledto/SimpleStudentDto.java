package pl.kurs.persondiary.dto.simpledto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
