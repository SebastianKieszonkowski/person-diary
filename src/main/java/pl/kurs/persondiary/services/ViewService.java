package pl.kurs.persondiary.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {
    private final JdbcTemplate jdbcTemplate;
    private static final String birthDate = "CASE WHEN SUBSTRING(pesel, 3, 2) > '20' AND SUBSTRING(pesel, 3, 2) < '33' THEN " +
            "CAST(CONCAT('20', SUBSTRING(pesel, 1, 2), '-', SUBSTRING(pesel, 3, 2) - 20 , '-', SUBSTRING(pesel, 5, 2)) AS DATE) " +
            "ELSE CAST(CONCAT('19', SUBSTRING(pesel, 1, 2), '-', SUBSTRING(pesel, 3, 2), '-', SUBSTRING(pesel, 5, 2)) AS DATE) " +
            "END AS birth_date";

    private static final String INSERT_SQL = "CREATE OR REPLACE VIEW person_view AS " +
            "SELECT 'employee' AS type, id, first_name, last_name, pesel, height, weight, email, version, " + birthDate +", hire_date, position, salary, NULL as pension, NULL as worked_years, NULL as university_name, NULL as study_year," +
            " NULL as study_field, NULL as scholarship FROM employees " +
            "UNION " +
            "SELECT 'pensioner' AS type, id, first_name, last_name, pesel, height, weight, email, version, " + birthDate +", NULL as hire_date, NULL as position, NULL as salary, pension, worked_years, NULL as university_name," +
            " NULL as study_year, NULL as study_field, NULL as scholarship FROM pensioners " +
            "UNION " +
            "SELECT 'student' AS type, id, first_name, last_name, pesel, height, weight, email, version, " + birthDate +", NULL as hire_date, NULL as position, NULL as salary, NULL as pension, NULL as worked_years, university_name, study_year," +
            " study_field, scholarship FROM students";

    @PostConstruct
    public void createView() {
        jdbcTemplate.execute(INSERT_SQL);

    }
}