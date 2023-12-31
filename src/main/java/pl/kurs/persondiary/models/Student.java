package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
                   LocalDate birthdate, Integer version, String universityName, Integer studyYear, String studyField, Double scholarship) {
        super(firstName, lastName, pesel, height, weight, email, birthdate, version);
        this.universityName = universityName;
        this.studyYear = studyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }

    @Override
    public String toString() {
        return "Student{ " + super.toString() +
                "universityName='" + universityName + '\'' +
                ", studyYear=" + studyYear +
                ", studyField='" + studyField + '\'' +
                ", scholarship=" + scholarship +
                '}';
    }
}
