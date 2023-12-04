package pl.kurs.persondiary.modelfactory;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.dto.viewdto.PensionerViewDto;
import pl.kurs.persondiary.models.PersonView;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator {
    private final ModelMapper modelMapper;

    @Override
    public String getType() {
        return "PENSIONER";
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, PensionerViewDto.class);
    }
}
