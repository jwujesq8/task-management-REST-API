package com.test.api.exceptionHandler;

import com.test.api.entity.ResponseMessage;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseMessage> authExceptionHandler(AuthException aE){
        String message = aE.getMessage();
        HttpStatus httpStatus;
        if(message.contains("User not found")) { httpStatus = HttpStatus.NOT_FOUND;}
        else if (message.contains("Wrong password")) {httpStatus = HttpStatus.UNAUTHORIZED;}
        else if (message.contains("User is already logged in")) {httpStatus = HttpStatus.OK;}
        else if (message.contains("Non valid refresh token")) {httpStatus = HttpStatus.BAD_REQUEST;}
        else if (message.contains("User is already logged out")) {httpStatus = HttpStatus.OK;}
        else if (message.contains("Wrong refresh token")) {httpStatus = HttpStatus.FORBIDDEN;}
        else httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(httpStatus)
                .body(new ResponseMessage(message, httpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseMessage> httpClientErrorExceptionHandler(HttpClientErrorException httpClErrEx){
        String message = httpClErrEx.getMessage();
        HttpStatus httpStatus = (HttpStatus) httpClErrEx.getStatusCode();

//        Gender not found
//        Wrong gender, id:
//        User not found, id:
//        User not found, id:

        return ResponseEntity
                .status(httpStatus)
                .body(new ResponseMessage(message, httpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseMessage> validationExceptionHandler(ValidationException valEx){
        String message = valEx.getMessage();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

//        Gender id and name must be not blank and empty.
    //        id: name
    //        1: male
    //        2: female
    //        3: none

//        Non valid gender name
//        Non valid user parameters

        return ResponseEntity
                .status(httpStatus)
                .body(new ResponseMessage(message, httpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ResponseMessage> serverErrorExceptionHandler(ServerErrorException serErrEx){
        String message = serErrEx.getMessage();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

//        Server error with Gender Table
//        Server error with removing users with id in range
//        Server error with User Table

        return ResponseEntity
                .status(httpStatus)
                .body(new ResponseMessage(message, httpStatus.getReasonPhrase()));

    }



}


