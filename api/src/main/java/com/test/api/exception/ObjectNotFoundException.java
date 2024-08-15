package com.test.api.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
