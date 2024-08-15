package com.test.api.exception;

public class UserAuthenticationException extends RuntimeException{
    public UserAuthenticationException(String errorMessage){
        super(errorMessage);
    }
}
