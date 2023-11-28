package pl.kurs.persondiary.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.persondiary.models.Employee;

import java.time.LocalDate;

@Service
public class PersonToEmployeeConverter2 implements Converter<String, Employee> {
    private final ObjectMapper objectMapper;

    public PersonToEmployeeConverter2(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Employee convert(MappingContext<String, Employee> mappingContext) {

        JsonNode source = objectMapper.readTree(mappingContext.getSource());
        return Employee.builder()
                .firstName(source.get("firstName").asText())
                .lastName(source.get("lastName").asText())
                .pesel(source.get("pesel").asText())
                .height(source.get("height").asDouble())
                .weight(source.get("weight").asDouble())
                .email(source.get("email").asText())
                .hireDate(LocalDate.now())   // do poprawy
                .position(source.get("position").asText())
                .salary(source.get("salary").asDouble())
                .build();
    }



//    @Override
//    public CreateEmployCommand convert(MappingContext<JsonNode, CreateEmployCommand> mappingContext) {
//
//        return CreateEmployCommand.builder()
//                .firstName(source.get("firstName").asText())
//                .lastName(source.get("lastName").asText())
//                .pesel(source.get("pesel").asText())
//                .height(source.get("height").asDouble())
//                .weight(source.get("weight").asDouble())
//                .email(source.get("email").asText())
//                .hireDate(LocalDate.now())   // do poprawy
//                .position(source.get("position").asText())
//                .salary(source.get("salary").asDouble())
//                .build();
//    }
}
