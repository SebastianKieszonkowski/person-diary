package pl.kurs.persondiary.dto.singledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullPensionerDto {
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
