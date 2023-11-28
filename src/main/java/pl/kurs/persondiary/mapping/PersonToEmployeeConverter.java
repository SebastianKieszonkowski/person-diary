package pl.kurs.persondiary.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import pl.kurs.persondiary.models.Employee;

import java.util.Map;

public class PersonToEmployeeConverter implements Converter<Map<String,Object>, Employee> {
    @Override
    public Employee convert(MappingContext<Map<String, Object>, Employee> mappingContext) {
        Map<String, Object> source = mappingContext.getSource();
        System.out.println("Zalapał");
//        return Employee.builder()
//                .id(null)
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
        return null;
    }
}

    //    @Override
//    public Employee convert(MappingContext<JsonNode, Employee> mappingContext) {
//        JsonNode source = mappingContext.getSource();
//        System.out.println("PersonToEmployeeConverter");
//        return Employee.builder()
//                .id(null)
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

