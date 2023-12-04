package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "positions")
public class EmployeePosition implements Identificationable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String positionName;

    @Column(nullable = false)
    private LocalDate startDateOnPosition;

    private LocalDate endDateOnPosition;

    @Column(nullable = false)
    private Double salary;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public EmployeePosition(String positionName, LocalDate startDateOnPosition, LocalDate endDateOnPosition, Double salary, Employee employee) {
        this.positionName = positionName;
        this.startDateOnPosition = startDateOnPosition;
        this.endDateOnPosition = endDateOnPosition;
        this.salary = salary;
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePosition that = (EmployeePosition) o;
        return Objects.equals(id, that.id) && Objects.equals(positionName, that.positionName) && Objects.equals(startDateOnPosition, that.startDateOnPosition) && Objects.equals(endDateOnPosition, that.endDateOnPosition) && Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, positionName, startDateOnPosition, endDateOnPosition, salary);
    }
}
//todo dodać unikalny klucz do stanowisk, składany albo dodatkowe pole