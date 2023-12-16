package pl.kurs.persondiary.config;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfig implements AsyncConfigurer {

    @Bean
    public ModelMapper getModelMapper(Set<Converter> converters) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);//STRICT
        converters.forEach(mapper::addConverter);
        return mapper;
    }

    @Override
    public Executor getAsyncExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Ustawienie liczby wątków core
        executor.setMaxPoolSize(20);  // Maksymalna liczba wątków
        executor.setQueueCapacity(50); // Pojemność kolejki
        executor.setKeepAliveSeconds(60); // Czas życia wątków powyżej corePoolSize
        executor.setThreadNamePrefix("Async-Executor-");
        executor.initialize();
        return executor;
    }
}
