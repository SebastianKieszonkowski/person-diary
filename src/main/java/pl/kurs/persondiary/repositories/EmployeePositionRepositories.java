package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.EmployeePosition;

public interface EmployeePositionRepositories extends JpaRepository<EmployeePosition, Long> {
}
