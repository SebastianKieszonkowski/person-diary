package pl.kurs.persondiary.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.services.personservices.IManagementService;
import pl.kurs.persondiary.services.querybuilder.QueryFactoryComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final FactoryPersonService factoryPersonService;
    private final QueryFactoryComponent queryFactoryComponent;
    private final PersonFactory personFactory;

    @Transactional
    public Person savePerson(Person person) {
        IManagementService<Person> personService = factoryPersonService.prepareManager(person.getClass().getSimpleName());
        Person savedPerson = personService.add(person);
        return savedPerson;
    }

    public Person updatePerson(Person person) {
        IManagementService<Person> updatePersonService = factoryPersonService.prepareManager(person.getClass().getSimpleName());
        Person editedPerson = updatePersonService.edit(person);
        return editedPerson;
    }

    @Transactional(readOnly = true)
    public Person getPersonByTypeAndPesel(String pesel, String type) {
        IManagementService<Person> updatePersonService = factoryPersonService.prepareManager(type);
        return updatePersonService.findByPesel(pesel);
    }

    @Transactional(readOnly = true)
    public boolean isPersonExists(String pesel, String type) {
        IManagementService<Person> personService = factoryPersonService.prepareManager(type);
        return personService.existsByPesel(pesel);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public PageImpl findPersonByParameters(Map<String, String> query, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        List<Predicate> predicates = new ArrayList<>();

        query.forEach((key, value) -> {
            queryFactoryComponent.buildPredicate(key, builder, root, value)
                    .ifPresent(predicates::add);
        });
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Person> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Person> personList = typedQuery.getResultList();

        List<ISimplePersonDto> personSimpleDto;
        personSimpleDto = personList.stream()
                .map(personFactory::createSimpleDtoFromPerson)
                .collect(Collectors.toList());

        long total = getTotalInfo(builder);

        return new PageImpl<>(personSimpleDto, pageable, total);
    }

    private long getTotalInfo(CriteriaBuilder builder) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Person> countRoot = countQuery.from(Person.class);
        List<Predicate> countPredicates = new ArrayList<>();

        countQuery.select(builder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        TypedQuery<Long> countTypedQuery = entityManager.createQuery(countQuery);
        long total = countTypedQuery.getSingleResult();
        return total;
    }
}
