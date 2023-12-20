package pl.kurs.persondiary.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"employee"})
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

}