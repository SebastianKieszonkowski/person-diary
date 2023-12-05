package pl.kurs.persondiary.services.singleservice;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.services.IManagementService;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ServiceManager {
    private final Map<String, IManagementService> creators;

    public ServiceManager(Set<IManagementService> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(IManagementService::getType, Function.identity()));
    }

    public IManagementService prepareManager(Person person) {
        return creators.get(person.getClass().getSimpleName().toUpperCase(Locale.ROOT));
    }
}
