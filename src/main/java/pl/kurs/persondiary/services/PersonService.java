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
import pl.kurs.persondiary.command.CreatePersonCommand;
import pl.kurs.persondiary.command.FindPersonQuery;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.factory.PersonFactory;
import pl.kurs.persondiary.models.*;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.services.entityservices.EmployeeService;
import pl.kurs.persondiary.services.entityservices.IManagementService;
import pl.kurs.persondiary.services.entityservices.PensionerService;
import pl.kurs.persondiary.services.entityservices.StudentService;

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
    private final ServiceFactory serviceFactory;
    private final PersonViewRepository personViewRepository;
    private final PersonFactory personFactory;
    private final EmployeeService employeeService;
    private final PensionerService pensionerService;
    private final StudentService studentService;
    private final ProgressService progressService;

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

    //@Transactional
    @SneakyThrows
    public Person updatePerson(String pesel, CreatePersonCommand updatePersonCommand) {
        if (!personViewRepository.existsByPeselAndType(pesel, updatePersonCommand.getType()))
            throw new ResourceNotFoundException("Result not found");
        IManagementService<Person> personService2 = serviceFactory.prepareManager(updatePersonCommand.getType());
        Person dbPerson = personService2.findByPesel(pesel);
        dbPerson = personFactory.update(dbPerson, updatePersonCommand);
        Person editedPerson = personService2.add(dbPerson);
        return dbPerson;
    }

    @Transactional(readOnly = true)
    public List<PersonView> findPersonByParameters(FindPersonQuery query, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonView> criteriaQuery = builder.createQuery(PersonView.class);
        Root<PersonView> root = criteriaQuery.from(PersonView.class);
        List<Predicate> predicates = new ArrayList<>();

        if (query.getType() != null) {
            String type = "%" + query.getType().toLowerCase(Locale.ROOT) + "%";
            predicates.add(builder.like(builder.lower(root.get("type")), type));
        }
        if (query.getFirstName() != null) {
            String firstName = "%" + query.getFirstName().toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("firstName")), firstName));
        }
        if (query.getLastName() != null) {
            String lastName = "%" + query.getLastName().toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("lastName")), lastName));
        }
        if (query.getPesel() != null) {
            predicates.add(builder.equal(root.get("pesel"), query.getPesel()));
        }
        if (query.getEmail() != null) {
            String email = "%" + query.getEmail().toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("email")), email));
        }
        if (query.getHeightFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("height"), query.getHeightFrom()));
        }
        if (query.getHeightTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("height"), query.getHeightTo()));
        }
        if (query.getWeightFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("weight"), query.getWeightFrom()));
        }
        if (query.getWeightTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("weight"), query.getWeightFrom()));
        }
        if (query.getGender() != null) {
            if (query.getGender().toString().equals("female")) {
                predicates.add(builder.like(builder.lower(root.get("firstName")), "%a"));
            } else {
                predicates.add(builder.notLike(builder.lower(root.get("firstName")), "%a"));
            }
        }
        if (query.getAgeFrom() != null) {
            LocalDate dateFrom = LocalDate.now().minusYears(query.getAgeFrom());
            predicates.add(builder.lessThanOrEqualTo(root.get("birthDate"), dateFrom));
        }
        if (query.getAgeTo() != null) {
            LocalDate dateTo = LocalDate.now().minusYears(query.getAgeTo());
            predicates.add(builder.greaterThanOrEqualTo(root.get("birthDate"), dateTo));
        }
        if (query.getSalaryFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("salary"), query.getSalaryFrom()));
        }
        if (query.getSalaryTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("salary"), query.getSalaryTo()));
        }
        if (query.getUniversityName() != null) {
            String universityName = "%" + query.getUniversityName().toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("universityName")), universityName));
        }
        if (query.getWorkedYearsFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("workedYears"), query.getWorkedYearsFrom()));
        }
        if (query.getWorkedYearsTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("workedYears"), query.getWorkedYearsTo()));
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
