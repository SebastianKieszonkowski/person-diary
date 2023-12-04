package pl.kurs.persondiary.modelfactory;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.IPersonDto;
import pl.kurs.persondiary.dto.singledto.FullStudentDto;
import pl.kurs.persondiary.dto.viewdto.StudentViewDto;
import pl.kurs.persondiary.models.PersonView;

@Service
@RequiredArgsConstructor
public class StudentsCreator implements PersonCreator {
    private final ModelMapper modelMapper;

    @Override
    public String getType() {
        return "STUDENT";
    }

    @Override
    public IPersonDto createDtoFromView(PersonView personView) {
        return modelMapper.map(personView, StudentViewDto.class);
    }
}
