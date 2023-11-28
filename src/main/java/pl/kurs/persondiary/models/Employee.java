package pl.kurs.persondiary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
//zastanowić się czy pozostałych pól nie potraktować jak osobne tabele, może to będzie łatwiej
public class Employee extends Person{
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Double salary;

    @Transient
    private List<EmployeePosition> employeePositions = new ArrayList<>();

    @Builder
    public Employee(Long id, String firstName, String lastName, String pesel,
                    Double height, Double weight, String email, LocalDate hireDate, String position, Double salary) {
        super(id, firstName, lastName, pesel, height, weight, email);
        this.hireDate = hireDate;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(hireDate, employee.hireDate) && Objects.equals(position, employee.position) && Objects.equals(salary, employee.salary) && Objects.equals(employeePositions, employee.employeePositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hireDate, position, salary, employeePositions);
    }
}
