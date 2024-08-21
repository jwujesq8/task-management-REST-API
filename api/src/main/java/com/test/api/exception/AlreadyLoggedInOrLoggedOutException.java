package com.test.api.exception;

public class AlreadyLoggedInOrLoggedOutException extends RuntimeException{
    public AlreadyLoggedInOrLoggedOutException(String errorMessage){
        super(errorMessage);
    }
}
