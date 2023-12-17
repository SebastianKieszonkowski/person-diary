package pl.kurs.persondiary.mapping;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.command.singleCommand.CreateEmployeeCommand;
import pl.kurs.persondiary.models.Employee;

@Service
public class CreateEmployeeCommandToEmployeeCreator implements Converter<CreateEmployeeCommand, Employee> {

    @Override
    public Employee convert(MappingContext<CreateEmployeeCommand, Employee> mappingContext) {
        CreateEmployeeCommand source = mappingContext.getSource();
        return new Employee(source.getFirstName()
                ,source.getLastName()
                ,source.getPesel()
                ,source.getHeight()
                ,source.getWeight()
                ,source.getEmail()
                ,0
                ,source.getHireDate()
                ,source.getPosition()
                ,source.getSalary());
    }
}
