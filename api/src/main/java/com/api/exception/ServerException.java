package com.api.exception;

public class ServerException extends RuntimeException{
    public ServerException(String errorMessage){
        super(errorMessage);
    }
}
