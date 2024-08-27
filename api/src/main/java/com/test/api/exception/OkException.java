package com.test.api.exception;

public class OkException extends RuntimeException{
    public OkException(String errorMessage){
        super(errorMessage);
    }

    public static class AlreadyLoggedInOrLoggedOutException extends OkException{
        public AlreadyLoggedInOrLoggedOutException(String errorMessage){
            super(errorMessage);
        }
    }

    public static class NoContentException extends OkException{
        public NoContentException(String errorMessage){
            super(errorMessage);
        }
    }
}
