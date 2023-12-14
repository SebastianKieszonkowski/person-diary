package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.pl.PESEL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString //do usuniecia w wersi ostatecznej
@Entity
@Table(name = "students")
public class Student extends Person {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(nullable = false)
    private String universityName;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer studyYear;

    @NotBlank
    @Column(nullable = false)
    private String studyField;

    @Positive
    @Column(nullable = false)
    private Double scholarship;

    public Student(@NotBlank String firstName, @NotBlank String lastName, @PESEL String pesel, @Positive Double height, @Positive Double weight, @Email String email, Integer version, @NotBlank String universityName, @Min(1) @Max(5) Integer studyYear, @NotBlank String studyField, @Positive Double scholarship) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.universityName = universityName;
        this.studyYear = studyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }
}
