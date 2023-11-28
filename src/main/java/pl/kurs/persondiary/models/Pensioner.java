package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@EqualsAndHashCode(exclude = {"serialVersionUID"})
@ToString //do usuniecia w wersi ostatecznej
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private Double pension;

    @Column(nullable = false)
    private Integer workedYears;

    public Pensioner(Long id, String firstName, String lastName, String pesel, Double height,
                     Double weight, String email, Double pension, Integer workedYears) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
