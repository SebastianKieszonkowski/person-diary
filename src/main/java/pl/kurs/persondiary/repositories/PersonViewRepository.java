package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.PersonView;

import java.util.Optional;

public interface PersonViewRepository extends JpaRepository<PersonView, Long> {

    Optional<PersonView> findByPeselAndType(String pesel, String type);

    boolean existsByPeselAndType(String pesel, String type);

    @Query("SELECT COUNT(p) FROM PersonView p")
    Long getTableSize();
}
