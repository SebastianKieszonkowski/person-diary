package pl.kurs.persondiary.repositories.singlerepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.Pensioner;

public interface PensionerRepositories extends JpaRepository<Pensioner, Long> {
}
