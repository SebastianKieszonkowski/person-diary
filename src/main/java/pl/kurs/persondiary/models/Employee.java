package pl.kurs.persondiary.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"employeePositions"})
@Entity
@Table(name = "employees")
public class Employee extends Person{
    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long id;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonIgnore
    private Set<EmployeePosition> employeePositions = new HashSet<>();

    @Builder

    public Employee(Long id, String firstName, String lastName, String pesel, Double height, Double weight, String email, LocalDate hireDate, String position, Double salary) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.hireDate = hireDate;
        this.position = position;
        this.salary = salary;
    }
}
