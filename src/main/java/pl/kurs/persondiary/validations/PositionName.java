package pl.kurs.persondiary.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {PositionNameValidator.class}
)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositionName {

    String message() default "Wrong position name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
