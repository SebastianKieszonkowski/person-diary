package pl.kurs.persondiary.services.importcsv;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.services.CommonService;

@Service
@RequiredArgsConstructor
public class PensionerImport implements PersonImport {

    private static final String INSERT_SQL = "insert into pensioners (id, first_name, last_name," +
            " pesel, height, weight, email, birthdate, version, pension, worked_years) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SEQUENCE_UPDATE_SQL = "update hibernate_sequences set next_val = next_val + 1 where sequence_name = ?";
    private static final String SEQUENCE_SELECT_SQL = "select next_val from hibernate_sequences where sequence_name = ?";

    @Override
    public String getType() {
        return "pensioner";
    }

    @Override
    public void importPerson(String[] args, JdbcTemplate jdbcTemplate) {
        String birthdate = CommonService.getBirthday(args[3]);

        String sequenceName = "default";
        jdbcTemplate.update(SEQUENCE_UPDATE_SQL, sequenceName);
        Long id = jdbcTemplate.queryForObject(SEQUENCE_SELECT_SQL, new Object[]{sequenceName}, Long.class);

        jdbcTemplate.update(INSERT_SQL, id, args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                args[6], birthdate, 0, Double.parseDouble(args[7]), Integer.parseInt(args[8]));
    }
}