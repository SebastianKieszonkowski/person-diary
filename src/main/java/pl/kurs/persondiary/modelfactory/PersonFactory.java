package pl.kurs.persondiary.modelfactory;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.exeptions.ExceptionResponseDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonFactory {

    private final Map<String, PersonCreator> creators;
    private final ObjectMapper objectMapper;

    public PersonFactory(Set<PersonCreator> creators, ObjectMapper objectMapper) {
        this.creators = creators.stream().collect(Collectors.toMap(PersonCreator::getType, Function.identity()));
        this.objectMapper = objectMapper;
    }

    public IPersonDto createDtoFromView(PersonView personView) {
        return creators.get(personView.getType().toUpperCase(Locale.ROOT)).createDtoFromView(personView);
    }

    public Person createPerson(String inputCreateCommand) {
        JsonNode personJsonNode = null;
        try {
            personJsonNode = objectMapper.readTree(inputCreateCommand);
        } catch (JacksonException e) {
            e.getMessage();
            new ExceptionResponseDto(List.of(e.getMessage()), "BAD_REQUEST", LocalDateTime.now());
        }

        return creators.get(personJsonNode.get("type").asText().toUpperCase(Locale.ROOT)).createPerson(personJsonNode);
    }

    public IPersonDto createDtoFromPerson(Person person) {
        return creators.get(person.getClass().getSimpleName().toUpperCase(Locale.ROOT)).createDtoFromPerson(person);
    }
}
