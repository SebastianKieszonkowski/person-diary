package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.services.entityservices.IManagementService;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ServiceFactory {
    private final Map<String, IManagementService> creators;

    public ServiceFactory(Set<IManagementService> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(IManagementService::getType, Function.identity()));
    }

    public IManagementService prepareManager(String type) {
        return creators.get(type.toUpperCase(Locale.ROOT));
    }
}
