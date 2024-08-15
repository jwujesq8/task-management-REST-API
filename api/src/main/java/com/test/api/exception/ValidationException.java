package com.test.api.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String errorMessage){
        super(errorMessage);
    }
}
