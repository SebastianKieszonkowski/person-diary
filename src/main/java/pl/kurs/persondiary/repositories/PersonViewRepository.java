package pl.kurs.persondiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.persondiary.models.PersonView;

public interface PersonViewRepository extends JpaRepository<PersonView, Long> {

    PersonView findByPesel(String pesel);
}
