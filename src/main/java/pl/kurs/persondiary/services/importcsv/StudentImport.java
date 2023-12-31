package pl.kurs.persondiary.services.importcsv;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.services.CommonService;

@Service
@RequiredArgsConstructor
public class StudentImport implements PersonImport{

    private static final String INSERT_SQL = "insert into students (first_name, last_name," +
            " pesel, height, weight, email, birthdate, version, university_name, study_year, study_field, scholarship) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public void importPerson(String[] args, JdbcTemplate jdbcTemplate) {
        String birthdate = CommonService.getBirthday(args[3]);
           jdbcTemplate.update(INSERT_SQL, args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                args[6], birthdate, 0, args[7], Integer.parseInt(args[8]), args[9], Double.parseDouble(args[10]));
    }
}