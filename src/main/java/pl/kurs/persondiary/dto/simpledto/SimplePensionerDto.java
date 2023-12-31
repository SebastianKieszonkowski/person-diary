package pl.kurs.persondiary.dto.simpledto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimplePensionerDto implements ISimplePersonDto {
    private String type;
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private Integer version;
    private Double pension;
    private Integer workedYears;
}
