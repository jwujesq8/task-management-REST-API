package com.test.api.exceptionHandler;

import com.test.api.dto.response.MessageResponseDto;
import com.test.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {


    // AUTHORIZATION

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<MessageResponseDto> tokenValidationExceptionHandler(
            TokenValidationException tokenValidationException){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new MessageResponseDto(tokenValidationException.getMessage()));
    }
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<MessageResponseDto> userAuthenticationExceptionHandler(UserAuthenticationException userAuthenticationException){

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponseDto(userAuthenticationException.getMessage()));
    }
    @ExceptionHandler(AlreadyLoggedInOrLoggedOutException.class)
    public ResponseEntity<MessageResponseDto> alreadyLoggedInOrLoggedOutExceptionHandler(
            AlreadyLoggedInOrLoggedOutException alreadyLoggedInOrLoggedOutException){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponseDto(alreadyLoggedInOrLoggedOutException.getMessage()));
    }




    // VALIDATION

    //FOR FIELDS VALIDATION ONLY
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> ValidationExceptionHandler(MethodArgumentNotValidException mANotValidEx){
//
//        Map<String, String> errors = new HashMap<>();
//
//        mANotValidEx.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(errors);
//
//    }
    // FOR FIELDS AND CLASSES VALIDATION
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationExceptionHandler(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            String errorMessage = error.getDefaultMessage();

            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else {
                fieldName = error.getObjectName();
            }

            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }




    // MODEL MAPPER

    @ExceptionHandler(ModelMappingException.class)
    public ResponseEntity<MessageResponseDto> modelMappingExceptionHandler(
            ModelMappingException modelMappingException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(modelMappingException.getMessage()));
    }




    // SERVICE

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MessageResponseDto> dataIntegrityViolationExceptionHandler(
            DataIntegrityViolationException dataIntegrityViolationException){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new MessageResponseDto("Data conflict: " +
                        dataIntegrityViolationException.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDto> unexpectedExceptionHandler(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto("Unexpected error: " + e.getMessage()));
    }
    @ExceptionHandler(OurMessagingException.class)
    public ResponseEntity<MessageResponseDto> ourMessagingExceptionHandler(MessagingException mEx){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(mEx.getMessage()));
    }
    @ExceptionHandler(OurDataAccessException.class)
    public ResponseEntity<MessageResponseDto> ourDataAccessExceptionHandler(
            OurMessagingException ourMessagingException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto("Error retrieving users from database: " +
                        ourMessagingException.getMessage()));
    }
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<MessageResponseDto> validationExceptionHandler(ValidException validException){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDto(validException.getMessage()));
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<MessageResponseDto> userAlreadyExistsExceptionHandler(
            UserAlreadyExistsException userAlreadyExistsException){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new MessageResponseDto(userAlreadyExistsException.getMessage()));
    }
    @ExceptionHandler(BadClientRequestException.class)
    public ResponseEntity<MessageResponseDto> badRequestExceptionHandler(
            BadClientRequestException badClientRequestException){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDto(badClientRequestException.getMessage()));

    }
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<MessageResponseDto> idNotFoundExceptionHandler(
            IdNotFoundException idNotFoundException){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDto(idNotFoundException.getMessage()));

    }
    @ExceptionHandler(ServerDBException.class)
    public ResponseEntity<MessageResponseDto> serverDBExceptionHandler(
            ServerDBException serverDBException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(serverDBException.getMessage()));
    }
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<MessageResponseDto> noContentExceptionHandler(
            NoContentException noContentException){
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new MessageResponseDto(noContentException.getMessage()));
    }
    @ExceptionHandler(OurServiceErrorException.class)
    public ResponseEntity<MessageResponseDto> serverExceptionHandler(
            OurServiceErrorException ourServiceErrorException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(ourServiceErrorException.getMessage()));
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<MessageResponseDto> objectNotFoundExceptionHandler(
            ObjectNotFoundException objectNotFoundException){
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new MessageResponseDto(objectNotFoundException.getMessage()));
    }


}


