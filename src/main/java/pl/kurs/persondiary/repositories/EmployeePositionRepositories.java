package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.EmployeePosition;

import java.time.LocalDate;
import java.util.List;

public interface EmployeePositionRepositories extends JpaRepository<EmployeePosition, Long> {

    @Query("SELECT ep FROM EmployeePosition ep " +
            "WHERE (ep.startDateOnPosition BETWEEN :startNewDate AND :endNewDate " +
            "OR ep.endDateOnPosition BETWEEN :startNewDate AND :endNewDate " +
            "OR :startNewDate BETWEEN ep.startDateOnPosition AND COALESCE(ep.endDateOnPosition, '2050-12-31')) " +
            "AND ep.employee.id = :employeeId")
    List<EmployeePosition> checkDates(LocalDate startNewDate, LocalDate endNewDate, Long employeeId);

    @Query("SELECT ep FROM EmployeePosition ep WHERE ep.employee.id = :employeeId AND ep.endDateOnPosition IS NULL")
    EmployeePosition getByEmployeeAndAndEndDateOnPosition(Long employeeId);
}
