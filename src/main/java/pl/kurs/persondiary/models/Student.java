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
public class Student extends Person{
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private Integer studyYear;

    @Column(nullable = false)
    private String studyField;

    @Column(nullable = false)
    private Double scholarship;

    public Student(Long id, String firstName, String lastName, String pesel, Double height, Double weight,
                   String email, String universityName, Integer studyYear, String studyField, Double scholarship) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.universityName = universityName;
        this.studyYear = studyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }
}
