package pl.kurs.persondiary;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
public class PersonDiaryApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(PersonDiaryApplication.class, args);
    }
}
