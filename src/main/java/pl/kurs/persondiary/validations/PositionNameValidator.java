package pl.kurs.persondiary.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PositionNameValidator implements ConstraintValidator<PositionName, String> {

    private List<String> positionNameList;

    @Override
    public void initialize(PositionName constraintAnnotation) {
        try {
            loadPositionNameDictionary();
        } catch (IOException e) {
            throw new IllegalStateException("Can't load file!", e);
        }
    }

    private void loadPositionNameDictionary() throws IOException {
        try (
                FileReader fr = new FileReader("src/main/resources/dictionaries/positionname.txt");
                BufferedReader br = new BufferedReader(fr, 8192)) {
            positionNameList = br.lines()
                    .map(x -> x.trim())
                    .map(x -> x.toLowerCase())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return false;
        return positionNameList.contains(s.toLowerCase(Locale.ROOT));
    }
}
