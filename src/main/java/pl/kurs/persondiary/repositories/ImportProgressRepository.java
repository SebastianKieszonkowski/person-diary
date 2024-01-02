package pl.kurs.persondiary.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.persondiary.models.ImportProgressInfo;

import java.util.Optional;

public interface ImportProgressRepository extends JpaRepository<ImportProgressInfo, Long> {

    @Query("SELECT i FROM ImportProgressInfo i WHERE i.task = :task")
    Optional<ImportProgressInfo> findByTask(String task);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByStatus(String status);

}
