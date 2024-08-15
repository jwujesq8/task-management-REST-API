package com.test.api.exception;

public class BadClientRequestException extends RuntimeException{
    public BadClientRequestException(String errorMessage){
        super(errorMessage);
    }
}
