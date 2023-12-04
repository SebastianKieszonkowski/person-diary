package pl.kurs.persondiary.services.singleservice;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.repositories.singlerepositories.EmployeeRepository;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

import java.util.List;

@Service
public class EmployeeService extends AbstractGenericManagementService<Employee, EmployeeRepository> {

    public EmployeeService(EmployeeRepository repository) {
        super(repository);
    }

//    @Transactional(readOnly = true)
//    public List<Employee> findAllPageable(Pageable pageable) {
//        int startPosition = pageable.getPageNumber() * pageable.getPageSize();
//        int stopPosition = (pageable.getPageNumber() + 1) * pageable.getPageSize();
//        return super.repository.findAll(startPosition, stopPosition);
//    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }


    public Employee findById(Long id){
        return repository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }
}
