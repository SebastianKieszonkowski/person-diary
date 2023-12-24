package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.Pensioner;

import java.util.Optional;

public interface PensionerRepositories extends JpaRepository<Pensioner, Long> {
    Optional<Pensioner> getByPesel(String pesel);
}
