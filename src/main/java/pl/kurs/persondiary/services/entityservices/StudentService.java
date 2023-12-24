package pl.kurs.persondiary.services.entityservices;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.repositories.StudentRepositories;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
public class StudentService extends AbstractGenericManagementService<Student, StudentRepositories> {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_SQL = "insert into student (first_name, last_name," +
            " pesel, height, weight, email,version, university_name, study_year, study_field, scholarship) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    public StudentService(StudentRepositories repository, JdbcTemplate jdbcTemplate, StudentRepositories studentRepositories) {
        super(repository);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getType() {
        return "student";
    }

    @Transactional(readOnly = true)
    public Page<Student> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @SneakyThrows
    public void addRecordFromFile(MultipartFile file) {
        Stream<String> lines = new BufferedReader(new InputStreamReader((file.getInputStream()))).lines();
        lines.map(line -> line.split(","))
                .forEach(args -> jdbcTemplate.update(INSERT_SQL, args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], args[7], Integer.parseInt(args[8]), args[9], Double.parseDouble(args[10])));
    }

}
