package com.test.api.exception;

public class NonValidTokenException extends RuntimeException{
    public NonValidTokenException(String errorMessage){
        super(errorMessage);
    }
}
