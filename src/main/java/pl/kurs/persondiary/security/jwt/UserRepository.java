package pl.kurs.persondiary.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.userRoles where u.username = :username")
    Optional<User> getUserByUsernameWithRoles(String username);

}
