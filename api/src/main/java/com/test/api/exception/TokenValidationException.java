package com.test.api.exception;

public class TokenValidationException extends RuntimeException{
    public TokenValidationException(String errorMessage){
        super(errorMessage);
    }
}
