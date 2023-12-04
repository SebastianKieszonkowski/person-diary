package pl.kurs.persondiary.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PeselService {
    public LocalDate getBirthDate(String pesel){
        int rawYear = Integer.parseInt(pesel.substring(0,1));
        int rawMonth = Integer.parseInt(pesel.substring(2,3));
        int rawDay = Integer.parseInt(pesel.substring(4,5));
        return LocalDate.of(getBirthYear(rawYear,rawMonth),getBirthMonth(rawMonth),rawDay);
    }

    private int getBirthYear(int inYear, int inMonth) {
        int year = inYear;
        if (inMonth > 80 && inMonth < 93) {
            year = 1800;
        }
        else if (inMonth > 0 && inMonth < 13) {
            year += 1900;
        }
        else if (inMonth> 20 && inMonth < 33) {
            year += 2000;
        }
        else if (inMonth > 40 && inMonth < 53) {
            year += 2100;
        }
        else if (inMonth > 60 && inMonth < 73) {
            year += 2200;
        }
        return year;
    }

    private int getBirthMonth(int inMonth) {
        int month = inMonth;
        if (inMonth > 80 && inMonth < 93) {
            month -= 80;
        }
        else if (inMonth > 20 && inMonth < 33) {
            month -= 20;
        }
        else if (inMonth > 40 && inMonth < 53) {
            month -= 40;
        }
        else if (inMonth > 60 && inMonth < 73) {
            month -= 60;
        }
        return month;
    }
}
