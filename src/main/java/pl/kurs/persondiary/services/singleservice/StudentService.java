package pl.kurs.persondiary.services.singleservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.models.Employee;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.repositories.singlerepositories.StudentRepositories;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

@Service
public class StudentService extends AbstractGenericManagementService<Student, StudentRepositories> {
    public StudentService(StudentRepositories repository) {
        super(repository);
    }
    @Transactional(readOnly = true)
    public Page<Student> findAllPageable(Pageable pageable) {
        return super.repository.findAll(pageable);
    }
}
