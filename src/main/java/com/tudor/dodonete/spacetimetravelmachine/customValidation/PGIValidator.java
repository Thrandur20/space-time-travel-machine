package com.tudor.dodonete.spacetimetravelmachine.customValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PGIValidator implements ConstraintValidator<PGIConstraint, String> {

    @Override
    public void initialize(PGIConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String pgi, ConstraintValidatorContext constraintValidatorContext) {
        return pgi.matches("[a-zA-Z][a-zA-Z0-9]{5,10}");
    }
}
