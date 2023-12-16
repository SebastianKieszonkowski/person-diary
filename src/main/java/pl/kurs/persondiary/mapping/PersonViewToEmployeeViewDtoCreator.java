package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.dto.viewdto.EmployeeViewDto;
import pl.kurs.persondiary.models.PersonView;

@Service
public class PersonViewToEmployeeViewDtoCreator implements Converter<PersonView, EmployeeViewDto> {

    @Override
    public EmployeeViewDto convert(MappingContext<PersonView, EmployeeViewDto> mappingContext) {
        PersonView source = mappingContext.getSource();
        return EmployeeViewDto.builder()
                .type(source.getType())
                .id(source.getSubId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pesel(source.getPesel())
                .height(source.getHeight())
                .weight(source.getWeight())
                .email(source.getEmail())
                .version(source.getVersion())
                .hireDate(source.getHireDate())
                .position(source.getPosition())
                .salary(source.getSalary())
                .build();
    }
}
