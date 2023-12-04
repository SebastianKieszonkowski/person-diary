package pl.kurs.persondiary.services.singleservice;

import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.repositories.singlerepositories.PensionerRepositories;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
public class PensionerService extends AbstractGenericManagementService<Pensioner, PensionerRepositories>{

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_SQL = "insert into pensioner (id, first_name, last_name, pesel, height, weight, email," +
            " pension, worked_years) values (default, ?, ?, ?, ?, ?, ?, ?, ?)";

    public PensionerService(PensionerRepositories repository, JdbcTemplate jdbcTemplate) {
        super(repository);
        this.jdbcTemplate = jdbcTemplate;
    }

    @SneakyThrows
    public void addRecordFromFile(MultipartFile file){
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .forEach(args -> jdbcTemplate.update(INSERT_SQL,args[1],args[2],args[3],Double.parseDouble(args[4]),Double.parseDouble(args[5]),
                        args[6],Double.parseDouble(args[7]),Integer.parseInt(args[8])));
    }

    @Override
    public void deleteAll() {
        super.repository.deleteAll();
    }

    @Override
    public String getType() {
        return "PENSIONER";
    }
}
