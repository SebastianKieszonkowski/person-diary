package pl.kurs.persondiary.services.personservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Student;
import pl.kurs.persondiary.repositories.StudentRepository;

@Service
public class StudentService extends AbstractGenericManagementService<Student, StudentRepository> {


    public StudentService(StudentRepository repository) {
        super(repository);
    }

    @Override
    public String getType() {
        return "student";
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Override
    public boolean existsByPesel(String pesel) {
        return repository.existsByPesel(pesel);
    }
}
