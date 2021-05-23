package com.drivers.suggestion.model.validator;

import com.drivers.suggestion.model.validator.constraints.IsLatitudeInFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatitudeValidator implements
        ConstraintValidator<IsLatitudeInFormat, Double> {
    @Override
    public void initialize(IsLatitudeInFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        if(aDouble == null)
            return false;
        return aDouble <= 90 && aDouble >= -90 || aDouble == -999;
    }
}
