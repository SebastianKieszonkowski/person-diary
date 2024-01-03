package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> getByPesel(String pesel);

    boolean existsByPesel(String pesel);

}
