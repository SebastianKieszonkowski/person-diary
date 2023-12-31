package pl.kurs.persondiary.services.importcsv;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ImportFactory {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, PersonImport> imports;

    public ImportFactory(Set<PersonImport> imports, JdbcTemplate jdbcTemplate) {
        this.imports = imports.stream().collect(Collectors.toMap(PersonImport::getType, Function.identity()));
        this.jdbcTemplate = jdbcTemplate;
    }

    public void importPerson(String[] args){
        imports.get(args[0].toLowerCase(Locale.ROOT)).importPerson(args, jdbcTemplate);
    }
}
