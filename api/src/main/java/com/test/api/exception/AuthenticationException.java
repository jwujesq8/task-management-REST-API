package com.test.api.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }

    public static class TokenValidationException extends AuthenticationException{
        public TokenValidationException(String errorMessage){
            super(errorMessage);
        }
    }

}
