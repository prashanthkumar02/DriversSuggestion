package com.drivers.suggestion.model.validator;

import com.drivers.suggestion.model.validator.constraints.IsLongitudeInFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongitudeValidator implements
        ConstraintValidator<IsLongitudeInFormat, Double> {
    @Override
    public void initialize(IsLongitudeInFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        if(aDouble == null)
            return false;
        return aDouble < 180 && aDouble > -180 || aDouble == -999;
    }
}
