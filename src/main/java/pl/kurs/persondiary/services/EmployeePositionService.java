package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.EmployeePosition;
import pl.kurs.persondiary.repositories.EmployeePositionRepositories;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeePositionService {
    private final EmployeePositionRepositories repository;

    public EmployeePositionService(EmployeePositionRepositories repositories) {
        this.repository = repositories;
    }

    public EmployeePosition findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with Id: " + id));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public EmployeePosition add(EmployeePosition employeePosition) {
        return repository.saveAndFlush(employeePosition);
    }

    public EmployeePosition editActualPosition(EmployeePosition employeePosition) {
        Long id = employeePosition.getEmployee().getId();
        EmployeePosition positionToUpdate = repository.getByEmployeeAndEndDateOnPosition(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with Id: " + id));
        employeePosition.setId(positionToUpdate.getId());

        if (!employeePosition.equals(positionToUpdate)) {
            positionToUpdate = repository.saveAndFlush(employeePosition);
        }
        return positionToUpdate;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<EmployeePosition> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EmployeePosition> checkDates(LocalDate startNewDate, LocalDate endNewDate, Long employeeId) {
        return repository.checkDates(startNewDate, endNewDate, employeeId);
    }

}
