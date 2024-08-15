package com.test.api.exception;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
