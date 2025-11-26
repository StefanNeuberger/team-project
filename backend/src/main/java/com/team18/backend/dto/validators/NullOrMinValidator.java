package com.team18.backend.dto.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrMinValidator implements ConstraintValidator<NullOrMin, Integer> {

    private int min;

    @Override
    public boolean isValid( Integer value, ConstraintValidatorContext constraintValidatorContext ) {
        if ( value == null ) return true;
        return value >= min;
    }

    @Override
    public void initialize( NullOrMin constraintAnnotation ) {
        ConstraintValidator.super.initialize( constraintAnnotation );
        this.min = constraintAnnotation.min();
    }
}
