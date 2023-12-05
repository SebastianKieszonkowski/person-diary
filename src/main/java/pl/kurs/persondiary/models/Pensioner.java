package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString //do usuniecia w wersi ostatecznej
@Entity
@Table(name = "pensioners")
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;

    @Positive
    @Column(nullable = false)
    private Double pension;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer workedYears;

    @Builder
    public Pensioner(Long id, String firstName, String lastName, String pesel, Double height, Double weight, String email, Double pension, Integer workedYears) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }

    public Pensioner(String firstName, String lastName, String pesel, Double height, Double weight, String email, Double pension, Integer workedYears) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
