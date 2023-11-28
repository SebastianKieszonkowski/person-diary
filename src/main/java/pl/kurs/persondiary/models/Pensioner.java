package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString //do usuniecia w wersi ostatecznej
@Entity

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pensioner pensioner = (Pensioner) o;
        return Objects.equals(pension, pensioner.pension) && Objects.equals(workedYears, pensioner.workedYears);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pension, workedYears);
    }
}
