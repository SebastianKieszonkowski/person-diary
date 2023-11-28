package pl.kurs.persondiary.dto.singledto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FullEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private LocalDate hireDate;
    private String position;
    private Double salary;
    private Integer positionHistory;
}
