package pl.kurs.persondiary.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, Integer> {

    Optional<UserRole> findFirstByName(String name);
}
