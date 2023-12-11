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
//People
//TYP,Imie,Nazwisko,Pesel,Wzrost,Waga,Email,Data_Zatrudnienia/Emerytury/Start_Studiow,Stanowisko/Rok_Studiow/Kierunek,Stypendium/Pensja/Doswiadczenie
//Student
//TYP,Imie,Nazwisko,Pesel,Wzrost,Waga,Email,Nazwa_Uczelni,Rok_Studiow,Kierunek,Stypendium
//Employee csv
//TYP,Imie,Nazwisko,Pesel,Wzrost,Waga,Email,Data_Zatrudnienia,Stanowisko,Pensja
//Pensioner
//TYP,Imie,Nazwisko,Pesel,Wzrost,Waga,Email,Emerytura,Doswiadczenie