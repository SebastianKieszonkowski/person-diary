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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.*;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.services.entityservices.EmployeeService;
import pl.kurs.persondiary.services.entityservices.IManagementService;
import pl.kurs.persondiary.services.entityservices.PensionerService;
import pl.kurs.persondiary.services.entityservices.StudentService;
import pl.kurs.persondiary.services.querybuilder.QueryFactoryComponent;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class PersonService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final PersonViewRepository personViewRepository;
    private final ServiceFactory serviceFactory;
    private final PersonFactory personFactory;

    private final EmployeeService employeeService;
    private final PensionerService pensionerService;
    private final StudentService studentService;

    private final ProgressService progressService;

    private final QueryFactoryComponent queryFactoryComponent;

    public void importPerson(String line, AtomicLong counter, String taskId) {
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

    @Modifying
    public Person savePerson(Person person) {
        IManagementService<Person> personService = serviceFactory.prepareManager(person.getClass().getSimpleName());
        Person savedPerson = personService.add(person);
        return savedPerson;
    }

    public Person updatePerson(Person person) {
        IManagementService<Person> updatePersonService = serviceFactory.prepareManager(person.getClass().getSimpleName());
        Person editedPerson = updatePersonService.edit(person);
        return editedPerson;
    }

    @Transactional(readOnly = true)
    public PersonView getPersonByTypeAndPesel(String pesel, String type){
        return personViewRepository.findByPeselAndType(pesel, type)
                .orElseThrow(() -> new ResourceNotFoundException("The resource to modify does not exist!"));
    }

    @Transactional(readOnly = true)
    public boolean isPersonExists(String pesel, String type){
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
}
