package pl.kurs.persondiary.services.entityservices;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.EmployeePositionRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeePositionService {
    private final EmployeePositionRepositories repository;

    public EmployeePositionService(EmployeePositionRepositories repositories) {
        this.repository = repositories;
    }

    public Optional<EmployeePosition> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public EmployeePosition add(EmployeePosition employeePosition) {
        return repository.saveAndFlush(employeePosition);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<EmployeePosition> getAll() {
        return repository.findAll();
    }

    public List<EmployeePosition> checkDates(LocalDate startNewDate, LocalDate endNewDate, Long employeeId){
        return repository.checkDates(startNewDate, endNewDate, employeeId);
    };
}
