package com.htc.fleetmanagement.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


// Custom annotation for validating email format
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = MailValidatorImpl.class)
public @interface MailValidator {
    String message() default "Invalid email format";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}