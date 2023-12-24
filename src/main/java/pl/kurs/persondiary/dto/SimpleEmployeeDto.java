package pl.kurs.persondiary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SimpleEmployeeDto extends RepresentationModel<SimpleEmployeeDto> implements ISimplePersonDto {
    private String type;
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double height;
    private Double weight;
    private String email;
    private Integer version;
    private LocalDate hireDate;
    private String position;
    private Double salary;
    private String positionsHistory;
}
