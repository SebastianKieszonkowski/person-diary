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
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.repositories.PersonViewRepository;
import pl.kurs.persondiary.services.entityservices.IManagementService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ServiceFactory serviceFactory;
    private final PersonViewRepository personViewRepository;
    private final PersonFactory personFactory;

    @Modifying
    public Person savePerson(Person person) {
        IManagementService<Person> personService = serviceFactory.prepareManager(person.getClass().getSimpleName());
        Person savedPerson = personService.add(person);
        return savedPerson;
    }

    //@Transactional
    @SneakyThrows
    public Person updatePerson(String pesel, CreatePersonCommand updatePersonCommand) {
        if( !personViewRepository.existsByPeselAndType(pesel, updatePersonCommand.getType()))
            throw new ResourceNotFoundException("Result not found");
        IManagementService<Person> personService2 = serviceFactory.prepareManager(updatePersonCommand.getType());
        Person dbPerson = personService2.findByPesel(pesel);
        dbPerson = personFactory.update(dbPerson, updatePersonCommand);
        Thread.sleep(10000);
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
            String type = "%" + query.getType().toLowerCase() + "%";
            predicates.add(builder.equal(builder.lower(root.get("type")), type));
        }
        if (query.getFirstName() != null) {
            String firstName = "%" + query.getFirstName().toLowerCase() + "%";
            predicates.add(builder.equal(builder.lower(root.get("firstName")), firstName));
        }
        if (query.getLastName() != null) {
            String lastName = "%" + query.getLastName().toLowerCase() + "%";
            predicates.add(builder.equal(builder.lower(root.get("lastName")), lastName));
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

    //extends AbstractGenericManagementService<Person, PersonRepository> {

//    public PersonService(PersonRepository repository) {
//        super(repository);
//    }

//    public List<Person> getAllEmployee() {
//        return repository.takeAll();
//    }

//    public List<Employee>findEmployeeByCriteria(String firstName, String lastName){
//        return repository.findEmployeeByCriteria(firstName,lastName);
//    }

//    public Optional<List<Person>> getByFirstName(String firstName){
//        return repository.findByFirstName(firstName);
//    }
//
//    public Optional<List<Person>> getByLastName(String lastName){
//        return repository.findByLastName(lastName);
//    }
//
//    public List<Person> filterPersonByFilter(String filter) {
//        List<String> params = Arrays.asList(filter.split(":"));
//        System.out.println(params.get(1));
//        String temp = params.get(1);
//        if(params.get(1).contains(",")){
//            System.out.println("jest");
//            String[] str = temp.split("<|,|>");
//            System.out.println(str);
//        }
//        System.out.println(params + " " + params.size());
//        return Collections.emptyList();
//    }
//
//    public Optional<Employee> getByPesel(String pesel) {
//        return repository.findByPesel(pesel);
//    }
//
//    public Optional<Person> getById(Long id) {
//        return repository.findById(id);
//    }
//
//    public Optional<Employee> getEmployeeById(Long id) {
//        return repository.findEmployeeById(id);
//    }
}
