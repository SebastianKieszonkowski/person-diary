package pl.kurs.persondiary.services.importcsv;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.services.CommonService;

@Service
@RequiredArgsConstructor
public class EmployeeImport implements PersonImport {

    private static final String INSERT_SQL = "insert into employees (id, first_name, last_name," +
            " pesel, height, weight, email, birthdate, version, hire_date, position, salary) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ID_SQL = "select e.id from employees e where pesel = ?";
    private static final String INSERT_POSITION_SQL = "insert into positions (position_name, start_date_on_position," +
            " end_date_on_position, salary, employee_id) values (?, ?, ?, ?, ?)";
    private static final String SEQUENCE_UPDATE_SQL = "update hibernate_sequences set next_val = next_val + 1 where sequence_name = ?";
    private static final String SEQUENCE_SELECT_SQL = "select next_val from hibernate_sequences where sequence_name = ?";

    @Override
    public String getType() {
        return "employee";
    }

    @Override
    public void importPerson(String[] args, JdbcTemplate jdbcTemplate) {
        String pesel = args[3];
        String birthdate = CommonService.getBirthday(args[3]);
        String position = args[8];
        String hireDate = args[7];
        Double salary = Double.parseDouble(args[9]);

        String sequenceName = "default";
        jdbcTemplate.update(SEQUENCE_UPDATE_SQL, sequenceName);
        Long id = jdbcTemplate.queryForObject(SEQUENCE_SELECT_SQL, new Object[]{sequenceName}, Long.class);

        jdbcTemplate.update(INSERT_SQL, id, args[1], args[2], pesel, Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                args[6], birthdate, 0, hireDate, position, salary);
        Long employee_id = jdbcTemplate.queryForObject(GET_ID_SQL, Long.class, pesel);
        jdbcTemplate.update(INSERT_POSITION_SQL, position, hireDate, null, salary, employee_id);

    }
}