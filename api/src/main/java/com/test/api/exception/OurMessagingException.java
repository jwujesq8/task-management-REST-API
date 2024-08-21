package com.test.api.exception;

public class OurMessagingException extends RuntimeException{
    public OurMessagingException(String errorMessage){
        super(errorMessage);
    }
}
