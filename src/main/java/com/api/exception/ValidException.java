package com.api.exception;


import jakarta.validation.ConstraintViolationException;

public class ValidException extends RuntimeException {
    private final ConstraintViolationException constraintViolationException;

    public ValidException(ConstraintViolationException constraintViolationException) {
        super(constraintViolationException.getMessage());
        this.constraintViolationException = constraintViolationException;
    }

    public ConstraintViolationException getConstraintViolationException() {
        return constraintViolationException;
    }
}

