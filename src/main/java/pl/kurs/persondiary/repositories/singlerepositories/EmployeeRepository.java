package pl.kurs.persondiary.repositories.singlerepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
