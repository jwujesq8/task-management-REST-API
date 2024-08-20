package com.test.api.exceptionHandler;

import com.test.api.dto.response.MessageResponseDto;
import com.test.api.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);




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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> ValidationExceptionHandler(MethodArgumentNotValidException mANotValidEx){

        Map<String, String> errors = new HashMap<>();

        mANotValidEx.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
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















//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ValidationErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex){
//        List<ViolationDto> details =
//                ex.getConstraintViolations().stream()
//                        .map(e -> new ViolationDto(e.getPropertyPath().toString(), e.getMessage()))
//                        .toList();
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ValidationErrorResponseDto(details));
//    }









//    @ExceptionHandler(AuthException.class)
//    public ResponseEntity<MessageResponseDto> authExceptionHandler(AuthException aE){
//        String message = aE.getMessage();
//        HttpStatus httpStatus;
//        if(message.contains("User not found")) { httpStatus = HttpStatus.NOT_FOUND;}
//        else if (message.contains("Wrong password")) {httpStatus = HttpStatus.UNAUTHORIZED;}
//        else if (message.contains("User is already logged in")) {httpStatus = HttpStatus.OK;}
//        else if (message.contains("Non valid refresh token")) {httpStatus = HttpStatus.BAD_REQUEST;}
//        else if (message.contains("User is already logged out")) {httpStatus = HttpStatus.OK;}
//        else if (message.contains("Wrong refresh token")) {httpStatus = HttpStatus.FORBIDDEN;}
//        else httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        return ResponseEntity
//                .status(httpStatus)
//                .body(new MessageResponseDto(message, httpStatus.getReasonPhrase()));
//    }
//
//    @ExceptionHandler(HttpClientErrorException.class)
//    public ResponseEntity<MessageResponseDto> httpClientErrorExceptionHandler(HttpClientErrorException httpClErrEx){
//        String message = httpClErrEx.getMessage();
//        HttpStatus httpStatus = (HttpStatus) httpClErrEx.getStatusCode();
//
////        Gender not found
////        Wrong gender, id:
////        User not found, id:
////        User not found, id:
//
//        return ResponseEntity
//                .status(httpStatus)
//                .body(new MessageResponseDto(message, httpStatus.getReasonPhrase()));
//    }
//
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<MessageResponseDto> validationExceptionHandler(ValidationException valEx){
//        String message = valEx.getMessage();
//        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
//
////        Gender id and name must be not blank and empty.
//    //        id: name
//    //        1: male
//    //        2: female
//    //        3: none
//
////        Non valid gender name
////        Non valid user parameters
//
//        return ResponseEntity
//                .status(httpStatus)
//                .body(new MessageResponseDto(message, httpStatus.getReasonPhrase()));
//    }
//
//    @ExceptionHandler(ServerErrorException.class)
//    public ResponseEntity<MessageResponseDto> serverErrorExceptionHandler(ServerErrorException serErrEx){
//        String message = serErrEx.getMessage();
//        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//
////        Server error with Gender Table
////        Server error with removing users with id in range
////        Server error with User Table
//
//        return ResponseEntity
//                .status(httpStatus)
//                .body(new MessageResponseDto(message, httpStatus.getReasonPhrase()));
//
//    }



}


