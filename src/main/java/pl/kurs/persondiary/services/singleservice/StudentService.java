package pl.kurs.persondiary.services.singleservice;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.repositories.singlerepositories.StudentRepositories;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

@Service
public class StudentService extends AbstractGenericManagementService<Student, StudentRepositories> {
    public StudentService(StudentRepositories repository) {
        super(repository);
    }
}
