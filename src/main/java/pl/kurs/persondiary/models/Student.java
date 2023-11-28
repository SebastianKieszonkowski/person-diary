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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(universityName, student.universityName) && Objects.equals(studyYear, student.studyYear) && Objects.equals(studyField, student.studyField) && Objects.equals(scholarship, student.scholarship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), universityName, studyYear, studyField, scholarship);
    }
}
