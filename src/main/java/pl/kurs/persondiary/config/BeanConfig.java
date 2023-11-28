package pl.kurs.persondiary.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurs.persondiary.mapping.PersonToEmployeeConverter;

import java.text.SimpleDateFormat;
import java.util.Set;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper getModelMapper(Set<Converter> converters){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        converters.forEach(mapper::addConverter);
//        mapper.addConverter(new PersonToEmployeeConverter());
        return mapper;
    }

//    @Bean
//    public ObjectMapper create() {
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
//        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,false);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//
//        return mapper;
//    }
//    @Bean
//    public ObjectMapper getObjectMapper(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper;
//    }
}
