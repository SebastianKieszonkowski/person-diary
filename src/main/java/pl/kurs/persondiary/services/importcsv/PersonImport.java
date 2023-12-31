package pl.kurs.persondiary.services.importcsv;

import org.springframework.jdbc.core.JdbcTemplate;

public interface PersonImport {
    String getType();

    void importPerson(String[] args, JdbcTemplate jdbcTemplate);
}
