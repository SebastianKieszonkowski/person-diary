package pl.kurs.persondiary.validations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {PeselValidator.class}
)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pesel {

    String message() default "Nieprawidłowy PESEL!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
