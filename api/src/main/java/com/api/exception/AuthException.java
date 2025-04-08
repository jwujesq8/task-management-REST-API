package com.api.exception;

public class AuthException extends RuntimeException{
    public AuthException(String errorMessage){
        super(errorMessage);
    }
}
