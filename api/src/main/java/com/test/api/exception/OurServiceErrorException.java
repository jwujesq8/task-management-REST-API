package com.test.api.exception;

public class OurServiceErrorException extends RuntimeException{
    public OurServiceErrorException(String errorMessage){
        super(errorMessage);
    }
}
