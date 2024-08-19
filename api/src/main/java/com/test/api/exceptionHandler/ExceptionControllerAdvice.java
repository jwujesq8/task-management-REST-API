package com.test.api.exceptionHandler;

import com.test.api.dto.ViolationDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.response.ValidationErrorResponseDto;
import com.test.api.exception.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {


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

    @ExceptionHandler(ModelMappingException.class)
    public ResponseEntity<MessageResponseDto> modelMappingExceptionHandler(
            ModelMappingException modelMappingException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(modelMappingException.getMessage()));
    }

    @ExceptionHandler(OurServiceErrorException.class)
    public ResponseEntity<MessageResponseDto> serverExceptionHandler(
            OurServiceErrorException ourServiceErrorException){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto(ourServiceErrorException.getMessage()));
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

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<MessageResponseDto> objectNotFoundExceptionHandler(
            ObjectNotFoundException objectNotFoundException){
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new MessageResponseDto(objectNotFoundException.getMessage()));
    }

    @ExceptionHandler(NonValidTokenException.class)
    public ResponseEntity<MessageResponseDto> nonValidTokenExceptionHandler(NonValidTokenException nonValidTokenException){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponseDto(
                        nonValidTokenException.getMessage()
                ));

    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<MessageResponseDto> userAuthenticationExceptionHandler(UserAuthenticationException userAuthenticationException){

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponseDto(userAuthenticationException.getMessage()));
    }

    @ExceptionHandler(ValidException.class)
    @ResponseBody
    public ResponseEntity<MessageResponseDto> validationExceptionHandler(ValidException validException){
        final List<ViolationDto> violations = validException.getConstraintViolationException().getConstraintViolations().stream()
                .map(
                        violation -> new ViolationDto(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDto(new ValidationErrorResponseDto(violations).getViolations().toString()));
    }







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


