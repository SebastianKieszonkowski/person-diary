package pl.kurs.persondiary.mapping.tosimple;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.simpledto.SimpleStudentDto;
import pl.kurs.persondiary.models.Student;

@Service
public class StudentToSimpleStudentDtoCreator implements Converter<Student, SimpleStudentDto> {

    @Override
    public SimpleStudentDto convert(MappingContext<Student, SimpleStudentDto> mappingContext) {
        Student source = mappingContext.getSource();
        return new SimpleStudentDto("student"
                ,source.getId()
                ,source.getFirstName()
                ,source.getLastName()
                ,source.getPesel()
                ,source.getHeight()
                ,source.getWeight()
                ,source.getEmail()
                ,source.getVersion()
                ,source.getUniversityName()
                ,source.getStudyYear()
                ,source.getStudyField()
                ,source.getScholarship());

    }
}
