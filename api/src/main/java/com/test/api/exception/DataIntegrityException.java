package com.test.api.exception;

public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(String errorMessage){
        super(errorMessage);
    }
}
