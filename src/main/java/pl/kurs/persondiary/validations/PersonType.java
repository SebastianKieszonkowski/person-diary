package pl.kurs.persondiary.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {pl.kurs.persondiary.validations.PersonTypeValidator.class}
)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonType {

    String message() default "Wrong person type!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
