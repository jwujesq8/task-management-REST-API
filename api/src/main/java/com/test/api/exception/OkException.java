package com.test.api.exception;

public class OkException extends RuntimeException{
    public OkException(String errorMessage){
        super(errorMessage);
    }

}
