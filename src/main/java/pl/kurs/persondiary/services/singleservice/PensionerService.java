package pl.kurs.persondiary.services.singleservice;

import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Pensioner;
import pl.kurs.persondiary.repositories.singlerepositories.PensionerRepositories;
import pl.kurs.persondiary.services.AbstractGenericManagementService;

@Service
public class PensionerService extends AbstractGenericManagementService<Pensioner, PensionerRepositories> {
    public PensionerService(PensionerRepositories repository) {
        super(repository);
    }
}
