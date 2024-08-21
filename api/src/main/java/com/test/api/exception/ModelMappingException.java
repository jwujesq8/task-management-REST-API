package com.test.api.exception;

public class ModelMappingException extends RuntimeException{
    public ModelMappingException(String errorMessage) {
        super(errorMessage);
    }
}
