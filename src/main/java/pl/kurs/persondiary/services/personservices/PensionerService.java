package pl.kurs.persondiary.services.personservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.persondiary.exeptions.ResourceNotFoundException;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.repositories.PensionerRepository;

@Service
public class PensionerService extends AbstractGenericManagementService<Pensioner, PensionerRepository> {

    public PensionerService(PensionerRepository repository) {
        super(repository);
    }

    @Override
    public String getType() {
        return "pensioner";
    }

    public Pensioner findPersonByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Override
    @Transactional(readOnly = true)
    public Pensioner findByPesel(String pesel) {
        return repository.getByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Not found entity with pesel: " + pesel));
    }

    @Override
    public boolean existsByPesel(String pesel) {
        return repository.existsByPesel(pesel);
    }
}
