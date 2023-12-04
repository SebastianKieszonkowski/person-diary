package pl.kurs.persondiary.modelfactory;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonFactory {

    private final Map<String, PersonCreator> creators;

    public PersonFactory(Set<PersonCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(PersonCreator::getType, Function.identity()));
    }

    public IPersonDto createDtoFromView(PersonView personView) {
        return creators.get(personView.getType().toUpperCase(Locale.ROOT)).createDtoFromView(personView);
    }

}
