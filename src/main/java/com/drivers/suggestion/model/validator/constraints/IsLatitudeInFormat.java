package com.drivers.suggestion.model.validator.constraints;

import com.drivers.suggestion.model.validator.LatitudeValidator;
import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LatitudeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLatitudeInFormat {
    String message() default "Field cannot be neither blank, empty nor out of range, accepted range [-90, 90]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
