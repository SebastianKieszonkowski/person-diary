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
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.dto.simpledto.ISimplePersonDto;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.services.importcsv.ImportFactory;
import pl.kurs.persondiary.services.personservices.EmployeeService;
import pl.kurs.persondiary.services.personservices.IManagementService;
import pl.kurs.persondiary.services.personservices.PensionerService;
import pl.kurs.persondiary.services.personservices.StudentService;
import pl.kurs.persondiary.services.querybuilder.QueryFactoryComponent;
import pl.kurs.persondiary.services.querybuilder.QueryPensionerBuilderComponent;
import pl.kurs.persondiary.services.querybuilder.QueryStudentBuilderComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PersonService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final FactoryPersonService factoryPersonService;

    private final QueryStudentBuilderComponent queryStudentBuilderComponent;
    private final QueryPensionerBuilderComponent queryPensionerBuilderComponent;
    private final QueryFactoryComponent queryFactoryComponent;

    private final EmployeeService employeeService;
    private final PensionerService pensionerService;
    private final StudentService studentService;
    private final ProgressService progressService;

    private final ModelMapper modelMapper;

    private final ImportFactory importFactory;

    private final PersonFactory personFactory;

    private final AtomicBoolean isImportInProgress = new AtomicBoolean(false);

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

//    @Transactional(readOnly = true)
//    public boolean isPersonExists(String pesel, String type) {
//       return personRepository.existsByPeselAndType(pesel, type);
//    }

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

    @Async
    @Transactional
    public void processFileAsync(MultipartFile file, String taskId) {
        AtomicLong counter = new AtomicLong(0);

        if (isImportInProgress.compareAndSet(false, true)) {
            try {
                progressService.startImport(taskId);
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                    lines.forEach(line -> importPerson(line, counter, taskId));
                    progressService.completeImport(taskId);
                } catch (IOException | DuplicateKeyException e) {
                    progressService.logException(taskId, e);
                    progressService.abortedImport(taskId);
                    throw new RuntimeException("Error processing the file", e);
                }
            } finally {
                isImportInProgress.set(false);
            }
        }
    }


    private void importPerson(String line, AtomicLong counter, String taskId) {
        String[] args = line.split(",");
        importFactory.importPerson(args);
        Long processedLines = counter.incrementAndGet();
        progressService.updateProgress(taskId, processedLines);
    }

    public AtomicBoolean getIsImportInProgress() {
        return isImportInProgress;
    }
}
