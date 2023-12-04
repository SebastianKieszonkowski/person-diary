package pl.kurs.persondiary.mapping;

import jakarta.validation.Valid;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.kurs.persondiary.command.singleCommand.CreateStudentCommand;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.models.PersonView;
import pl.kurs.persondiary.models.Student;

@Service
@Validated
public class CreateStudentCommandToStudentCreator implements Converter<CreateStudentCommand, Student> {
    @Override
    public Student convert(@Valid MappingContext<CreateStudentCommand, Student> mappingContext) {
        CreateStudentCommand createStudentCommand = mappingContext.getSource();
        return Student.builder()
                .firstName(createStudentCommand.getFirstName())
                .lastName(createStudentCommand.getLastName())
                .pesel(createStudentCommand.getPesel())
                .height(createStudentCommand.getHeight())
                .weight(createStudentCommand.getWeight())
                .email(createStudentCommand.getEmail())
                .universityName(createStudentCommand.getUniversityName())
                .studyYear(createStudentCommand.getStudyYear())
                .studyField(createStudentCommand.getStudyField())
                .scholarship(createStudentCommand.getScholarship())
                .build();
    }
}
