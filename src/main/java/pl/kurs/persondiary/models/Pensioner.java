package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pensioners")
public class Pensioner extends Person {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private Double pension;
    @Column(nullable = false)
    private Integer workedYears;

    public Pensioner(String firstName, String lastName, String pesel, Double height, Double weight, String email,
                     LocalDate birthdate, Integer version, Double pension, Integer workedYears) {
        super(firstName, lastName, pesel, height, weight, email, birthdate, version);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
