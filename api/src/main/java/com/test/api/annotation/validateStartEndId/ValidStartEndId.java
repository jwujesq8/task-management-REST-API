package com.test.api.annotation.validateStartEndId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidStartEndIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStartEndId {
    String message() default "Must be a valid long number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
