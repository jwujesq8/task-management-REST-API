package com.test.api.exception;

public class ServerDBException extends RuntimeException{
    public ServerDBException(String errorMessage){
        super(errorMessage);
    }
}
