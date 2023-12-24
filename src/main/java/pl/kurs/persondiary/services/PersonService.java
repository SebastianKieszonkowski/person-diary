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
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.models.*;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.services.personservices.EmployeeService;
import pl.kurs.persondiary.services.personservices.IManagementService;
import pl.kurs.persondiary.services.personservices.PensionerService;
import pl.kurs.persondiary.services.personservices.StudentService;
import pl.kurs.persondiary.services.querybuilder.QueryFactoryComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PersonService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final QueryFactoryComponent queryFactoryComponent;
    private final FactoryPersonService factoryPersonService;

    private final EmployeeService employeeService;
    private final PensionerService pensionerService;
    private final StudentService studentService;
    private final ProgressService progressService;

    private final PersonViewRepository personViewRepository;

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

    @Transactional(readOnly = true)
    public boolean isPersonExists(String pesel, String type) {
        return personViewRepository.existsByPeselAndType(pesel, type);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<PersonView> findPersonByParameters(FindPersonQuery query, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonView> criteriaQuery = builder.createQuery(PersonView.class);
        Root<PersonView> root = criteriaQuery.from(PersonView.class);
        List<Predicate> predicates = new ArrayList<>();

        for (Field field : FindPersonQuery.class.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(query);
            if (value != null) {
                predicates.add(queryFactoryComponent.buildPredicate(field.getName(), builder, root, value));
            }
        }

        if (predicates.size() != 0) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        TypedQuery<PersonView> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<PersonView> personViewList = typedQuery.getResultList();

        return personViewList;
    }

    @Async
    public void processFileAsync(MultipartFile file, String taskId) {
        AtomicLong counter = new AtomicLong(0);

        if (isImportInProgress.compareAndSet(false, true)) {
            try {
                progressService.startImport(taskId);
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                    lines.forEach(line -> importPerson(line, counter, taskId));
                    progressService.completeImport(taskId);
                } catch (IOException e) {
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
        try {
            if (args[0].toLowerCase(Locale.ROOT).equals("employee")) {
                Employee employee = new Employee(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], 0, LocalDate.parse(args[7]), args[8], Double.parseDouble(args[9]));
                employeeService.add(employee);
            } else if (args[0].toLowerCase(Locale.ROOT).equals("student")) {
                Student student = new Student(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], 0, args[7], Integer.parseInt(args[8]), args[9], Double.parseDouble(args[10]));
                studentService.add(student);
            } else if (args[0].toLowerCase(Locale.ROOT).equals("pensioner")) {
                Pensioner pensioner = new Pensioner(args[1], args[2], args[3], Double.parseDouble(args[4]), Double.parseDouble(args[5]),
                        args[6], 0, Double.parseDouble(args[7]), Integer.parseInt(args[8]));
                pensionerService.add(pensioner);
            }
        } catch (Exception e) {
            progressService.logException(taskId, e);
        }

        Long processedLines = counter.incrementAndGet();
        progressService.updateProgress(taskId, processedLines);
    }

    public AtomicBoolean getIsImportInProgress() {
        return isImportInProgress;
    }
}
