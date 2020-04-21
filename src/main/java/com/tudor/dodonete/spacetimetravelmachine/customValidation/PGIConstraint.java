package com.tudor.dodonete.spacetimetravelmachine.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PGIValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PGIConstraint {
    String message() default "Invalid PGI";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
