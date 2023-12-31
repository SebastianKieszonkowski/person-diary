package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CommonService {

    public static String getBirthday(String pesel){
        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        int monthOffset = month / 20;
        month = month % 20;
        int century = (monthOffset == 0) ? 1900 : (monthOffset == 1) ? 2000 : 2100;
        year += century;
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
