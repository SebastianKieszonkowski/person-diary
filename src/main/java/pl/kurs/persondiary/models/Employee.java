package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.pl.PESEL;

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

    public Employee(Long id, @NotBlank String firstName, @NotBlank String lastName, @PESEL String pesel, @Positive Double height, @Positive Double weight, @Email String email, Integer version, @PastOrPresent LocalDate hireDate, @NotBlank String position, @Positive Double salary) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.hireDate = hireDate;
        this.position = position;
        this.salary = salary;
    }

    public Employee(@NotBlank String firstName, @NotBlank String lastName, @PESEL String pesel, @Positive Double height, @Positive Double weight, @Email String email, Integer version, @PastOrPresent LocalDate hireDate, @NotBlank String position, @Positive Double salary) {
        super(firstName, lastName, pesel, height, weight, email, version);
        this.hireDate = hireDate;
        this.position = position;
        this.salary = salary;
    }
}
