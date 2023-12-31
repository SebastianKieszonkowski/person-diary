package pl.kurs.persondiary.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.persondiary.services.CommonService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PeselValidator implements ConstraintValidator<Pesel, String> {

    @Override
    public void initialize(Pesel constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        if (!s.matches("\\d{11}")) {
            return false;
        }
        return isValidDate(extractDateFromPesel(s));
    }

    private String extractDateFromPesel(String pesel) {
        return CommonService.getBirthday(pesel);
    }

    private boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
