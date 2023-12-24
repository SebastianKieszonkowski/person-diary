package pl.kurs.persondiary.services.entityservices;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.repositories.PensionerRepositories;

@Service
public class PensionerService extends AbstractGenericManagementService<Pensioner, PensionerRepositories> {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_SQL = "insert into pensioners (id, first_name, last_name, pesel, height, weight, email, " +
            "version, pension, worked_years) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SEQUENCE_SQL = "SELECT nextval('HIBERNATE_SEQUENCES')";

    public PensionerService(PensionerRepositories repository, JdbcTemplate jdbcTemplate) {
        super(repository);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getType() {
        return "pensioner";
    }

    public Pensioner findPersonByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Override
    @Transactional(readOnly = true)
    public Pensioner findByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }
    //    @SneakyThrows
//    // @Async
//    public CompletableFuture<Void> addRecordFromFile(MultipartFile file) {
//        return CompletableFuture.runAsync(() -> {
//            try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
//                lines.map(line -> line.split(","))
//                        .forEach(args -> {
//                            Long nextId = jdbcTemplate.queryForObject(SEQUENCE_SQL, Long.class);
//                            jdbcTemplate.update(INSERT_SQL,nextId, args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5])
//                                    , 0, args[6], Double.parseDouble(args[7]), Integer.parseInt(args[8]));
//                            try {
//                                Thread.sleep(2000); // Zatrzymanie na 2 sekundy
//                            } catch (InterruptedException e) {
//                                Thread.currentThread().interrupt();
//                                throw new RuntimeException("Interrupted during sleep", e);
//                            }
//                        });
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

}
