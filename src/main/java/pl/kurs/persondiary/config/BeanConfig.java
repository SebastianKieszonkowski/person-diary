package pl.kurs.persondiary.config;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper getModelMapper(Set<Converter> converters){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        converters.forEach(mapper::addConverter);
        return mapper;
    }
}
