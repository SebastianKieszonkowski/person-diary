package pl.kurs.persondiary.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PersonTypeValidator implements ConstraintValidator<PersonType, String> {

    @Value("${types}")
    private List<String> types;

    @Override
    public void initialize(PersonType constraintAnnotation) {
        types = types.stream()
                .map(x -> x.trim())
                .map(x -> x.toLowerCase())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return false;
        return types.contains(s.toLowerCase(Locale.ROOT));
    }
}
