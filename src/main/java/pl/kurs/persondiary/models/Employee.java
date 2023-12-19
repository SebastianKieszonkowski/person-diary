package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"employeePositions"})
@Entity
@Table(name = "employees")
public class Employee extends Person {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private LocalDate hireDate;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EmployeePosition> employeePositions = new HashSet<>();

//    public Employee(Long id, @NotBlank String firstName, @NotBlank String lastName, @PESEL String pesel, @Positive Double height, @Positive Double weight, @Email String email, Integer version, @PastOrPresent LocalDate hireDate, @NotBlank String position, @Positive Double salary) {
//        super(id, firstName, lastName, pesel, height, weight, email, version);
//        this.hireDate = hireDate;
//        this.position = position;
//        this.salary = salary;
//    }

    public Employee(String firstName, String lastName, String pesel, Double height, Double weight, String email, Integer version,
                    LocalDate hireDate, String position, Double salary) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.hireDate = hireDate;
        this.position = position;
        this.salary = salary;
    }
}
