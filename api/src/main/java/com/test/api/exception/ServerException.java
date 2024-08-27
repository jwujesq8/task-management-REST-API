package com.test.api.exception;

public class ServerException extends RuntimeException{
    public ServerException(String errorMessage){
        super(errorMessage);
    }

    public static class ModelMappingException extends ServerException{
        public ModelMappingException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class OurMessagingException extends ServerException{
        public OurMessagingException(String errorMessage){
            super(errorMessage);
        }
    }

}
