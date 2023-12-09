package pl.kurs.persondiary.repositories.singlerepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions")
    List<Employee> findAll();

    //  @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.employeePositions")
//    //Page<Employee> findAllWithPagination(Pageable page);
    //  List<Employee> findAll(Integer startPosition, Integer stopPosition);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions WHERE e.id = :id")
    Optional<Employee> findById(Long id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.employeePositions WHERE e.pesel = :pesel")
    Optional<Employee> findByPesel(String pesel);
}
