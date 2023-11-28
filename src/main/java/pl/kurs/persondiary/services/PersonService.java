package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Person;
import pl.kurs.persondiary.repositories.PersonRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class PersonService extends AbstractGenericManagementService<Person, PersonRepository> {

    public PersonService(PersonRepository repository) {
        super(repository);
    }

//    public List<Person> getAllEmployee() {
//        return repository.takeAll();
//    }

//    public List<Employee>findEmployeeByCriteria(String firstName, String lastName){
//        return repository.findEmployeeByCriteria(firstName,lastName);
//    }

    public Optional<List<Person>> getByFirstName(String firstName){
        return repository.findByFirstName(firstName);
    }

    public Optional<List<Person>> getByLastName(String lastName){
        return repository.findByLastName(lastName);
    }

    public List<Person> filterPersonByFilter(String filter) {
        List<String> params = Arrays.asList(filter.split(":"));
        System.out.println(params.get(1));
        String temp = params.get(1);
        if(params.get(1).contains(",")){
            System.out.println("jest");
            String[] str = temp.split("<|,|>");
            System.out.println(str);
        }
        System.out.println(params + " " + params.size());
        return Collections.emptyList();
    }

    public Optional<Employee> getByPesel(String pesel) {
        return repository.findByPesel(pesel);
    }

    public Optional<Person> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return repository.findEmployeeById(id);
    }
}
