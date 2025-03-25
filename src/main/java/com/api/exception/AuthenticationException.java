package com.api.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }

}
