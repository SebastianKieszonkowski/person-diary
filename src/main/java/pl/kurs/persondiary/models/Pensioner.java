package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.pl.PESEL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Pensioner(@NotBlank String firstName, @NotBlank String lastName, @PESEL String pesel, @Positive Double height, @Positive Double weight, @Email String email, Integer version, @Positive Double pension, @PositiveOrZero Integer workedYears) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.pension = pension;
        this.workedYears = workedYears;
    }
}
