package pl.kurs.persondiary.services.importcsv;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.services.CommonService;

@Service
@RequiredArgsConstructor
public class EmployeeImport implements PersonImport {

    private static final String INSERT_SQL = "insert into employees (first_name, last_name," +
            " pesel, height, weight, email, birthdate, version, hire_date, position, salary) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ID_SQL = "select e.id from employees e where pesel = ?";
    private static final String INSERT_POSITION_SQL = "insert into positions (position_name, start_date_on_position," +
            " end_date_on_position, salary, employee_id) values (?, ?, ?, ?, ?)";

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

        jdbcTemplate.update(INSERT_SQL, args[1], args[2], pesel, Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                args[6] ,birthdate, 0, hireDate, position, salary);
        Long employee_id = jdbcTemplate.queryForObject(GET_ID_SQL, Long.class, pesel);
        jdbcTemplate.update(INSERT_POSITION_SQL, position, hireDate, null, salary, employee_id);

    }
}