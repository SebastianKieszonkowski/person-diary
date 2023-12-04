package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDate;

@Immutable
@Getter
@Setter
@Entity
@Subselect("SELECT * FROM person_view")
public class PersonView {

    @Column(name = "type")
    private String type;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "position")
    private String position;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "pension")
    private Double pension;

    @Column(name = "worked_years")
    private Integer workedYears;

    @Column(name = "university_name")
    private String universityName;

    @Column(name = "study_year")
    private Integer studyYear;

    @Column(name = "study_field")
    private String studyField;

    @Column(name = "scholarship")
    private Double scholarship;
}
