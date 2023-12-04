package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString //do usuniecia w wersi ostatecznej
@Entity
@Table(name = "students")
public class Student extends Person{
    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long id;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private Integer studyYear;

    @Column(nullable = false)
    private String studyField;

    @Column(nullable = false)
    private Double scholarship;

    public Student(Long id, String firstName, String lastName, String pesel, Double height, Double weight, String email, String universityName, Integer studyYear, String studyField, Double scholarship) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.universityName = universityName;
        this.studyYear = studyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }
}
