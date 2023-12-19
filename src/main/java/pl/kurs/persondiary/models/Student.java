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
@Table(name = "students")
public class Student extends Person {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String universityName;
    @Column(nullable = false)
    private Integer studyYear;
    @Column(nullable = false)
    private String studyField;
    @Column(nullable = false)
    private Double scholarship;

    public Student(String firstName, String lastName, String pesel, Double height, Double weight, String email,
                   Integer version, String universityName, Integer studyYear, String studyField, Double scholarship) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.universityName = universityName;
        this.studyYear = studyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }
}
