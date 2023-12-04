package pl.kurs.persondiary.dto.viewdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.persondiary.dto.IPersonDto;

@Getter
@Setter
@Builder
public class PensionerViewDto implements IPersonDto {
    private String type;
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
