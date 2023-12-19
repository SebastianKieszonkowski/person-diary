package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode
@ToString //do usuniecia w wersi ostatecznej
@Entity
@Table(name = "pensioners")
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private Double pension;
    @Column(nullable = false)
    private Integer workedYears;

    public Pensioner(String firstName, String lastName, String pesel, Double height, Double weight, String email, Integer version, Double pension, Integer workedYears) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
