package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions")
    List<Employee> findAll();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions WHERE e.id = :id")
    Optional<Employee> findById(Long id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions WHERE e.pesel = :pesel")
    Optional<Employee> getByPeselWithPositions(String pesel);

    @Query("SELECT e FROM Employee e WHERE e.pesel = :pesel")
    Optional<Employee> getByPesel(String pesel);
}
