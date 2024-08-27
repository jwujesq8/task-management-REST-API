package com.test.api.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException (String errorMessage){
        super(errorMessage);
    }

    public static class BadClientRequestException extends BadRequestException {
        public BadClientRequestException(String errorMessage){
            super(errorMessage);
        }
    }

    public static class IdNotFoundException extends BadRequestException{
        public IdNotFoundException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class ObjectNotFoundException extends BadRequestException{
        public ObjectNotFoundException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class UserAlreadyExistsException extends BadRequestException{
        public UserAlreadyExistsException(String errorMessage){
            super(errorMessage);
        }
    }
}
