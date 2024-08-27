package com.test.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.dto.response.ErrorMessageResponseDto;
import com.test.api.dto.response.ValidationErrorMessageResponseDto;
import com.test.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private ErrorMessageResponseDto getResponseBody(String errorMessage) throws JsonProcessingException {

        return ErrorMessageResponseDto.builder()
                .dateTime("UTC: " + formatter.format(Instant.now().atZone(ZoneId.of("UTC"))))
                .description(errorMessage)
                .build();
    }



    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageResponseDto> authenticationExceptionHandler(AuthenticationException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(getResponseBody(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessageResponseDto> badRequestExceptionHandler(BadRequestException e) throws JsonProcessingException {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getResponseBody(e.getMessage()));
    }

    @ExceptionHandler(OkException.class)
    public ResponseEntity<ErrorMessageResponseDto> okExceptionHandler(OkException e) throws JsonProcessingException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getResponseBody(e.getMessage()));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorMessageResponseDto> serverExceptionHandler(ServerException e) throws JsonProcessingException{
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getResponseBody(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponseDto> unexpectedExceptionHandler(
            Exception e) throws JsonProcessingException{

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getResponseBody(e.getMessage()));
    }

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ErrorMessageResponseDto> validationExceptionHandler(
            ValidException validException) throws JsonProcessingException{

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getResponseBody(validException.getMessage()));
    }



    // VALIDATION - FOR FIELDS AND CLASSES VALIDATION

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorMessageResponseDto> validationExceptionHandler(
            MethodArgumentNotValidException ex) throws JsonProcessingException {

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

        ValidationErrorMessageResponseDto validErrorMessageResponseDto = ValidationErrorMessageResponseDto.builder()
                .dateTime("UTC: " + formatter.format(Instant.now().atZone(ZoneId.of("UTC"))))
                .errorsMap(errors)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validErrorMessageResponseDto);
    }


}


