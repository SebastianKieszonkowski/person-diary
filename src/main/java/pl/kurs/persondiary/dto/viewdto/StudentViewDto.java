package pl.kurs.persondiary.dto.viewdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.dto.IPersonDto;

@Getter
@Setter
@Builder
public class StudentViewDto implements IPersonDto {
    private String type;
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
