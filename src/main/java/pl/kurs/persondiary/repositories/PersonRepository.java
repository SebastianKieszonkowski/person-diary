package pl.kurs.persondiary.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {//extends JpaRepository<Person, Long> {

    //@Query("SELECT e FROM Employee e")
//    List<Employee> takeAll();

    //@Query("SELECT e FROM Employee e WHERE (:firstName IS NULL OR e.firstName = :firstName AND :lastName IS NULL OR e.lastName= :lasttName)")
//    @Query("SELECT e FROM Employee e WHERE (:firstName IS NULL OR e.firstName = :firstName) ")
//    List<Employee> findEmployeeByCriteria(String firstName, String lastName);

//    Optional<List<Person>> findByFirstName(String firstName);
//
//    Optional<List<Person>> findByLastName(String lastName);
//
//    Optional<Employee> findByPesel(String pesel);
//
//    Optional<Employee> findEmployeeById(Long id);
}
