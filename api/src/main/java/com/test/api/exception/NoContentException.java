package com.test.api.exception;

public class NoContentException extends RuntimeException{
    public NoContentException(String errorMessage){
        super(errorMessage);
    }
}
