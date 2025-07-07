package com.dongVu1105.libraryManagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {QuantityValidator.class})
public @interface QuantityConstraint {
    String message() default "Invalid quantity of book";

    long min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
