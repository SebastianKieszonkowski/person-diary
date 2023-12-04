package pl.kurs.persondiary.dto.singledto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.dto.IPersonDto;

@Getter
@Setter
@Builder
public class FullPensionerDto implements IPersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private Double pension;
    private Integer workedYears;
}
