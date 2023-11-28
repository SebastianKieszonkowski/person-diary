package pl.kurs.persondiary.repositories.singlerepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.Student;

public interface StudentRepositories extends JpaRepository<Student, Long> {
}
