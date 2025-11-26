package com.team18.backend.dto.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public boolean isValid( String s, ConstraintValidatorContext constraintValidatorContext ) {
        return s == null || !s.trim().isEmpty();
    }
}
