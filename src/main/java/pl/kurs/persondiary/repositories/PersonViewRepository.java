package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.PersonView;

import java.util.Optional;

public interface PersonViewRepository extends JpaRepository<PersonView, Long> {

    //@Query("SELECT p FROM PersonView p WHERE p.pesel = :pesel AND p.type = :type")
    Optional<PersonView> findByPeselAndType(String pesel, String type);

    boolean existsByPeselAndType(String pesel, String type);
}
