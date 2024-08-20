package com.test.api.exception;

public class OurDataAccessException extends RuntimeException{
    public OurDataAccessException(String errorMessage){
        super(errorMessage);
    }
}
