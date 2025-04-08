package com.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.api.dto.error.ErrorMessageResponseDto;
import com.api.dto.error.ValidationErrorMessageResponseDto;
import com.api.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ExceptionControllerAdvice{

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    private ErrorMessageResponseDto getResponseBody(String errorMessage) throws JsonProcessingException {

        return ErrorMessageResponseDto.builder()
                .dateTime("UTC: " + formatter.format(Instant.now().atZone(ZoneId.of("UTC"))))
                .description(errorMessage)
                .build();
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessageResponseDto> badRequestExceptionHandler(BadRequestException e) throws JsonProcessingException {

        log.error("Exception: BadRequestException. " +
                "Exception message: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getResponseBody(e.getMessage()));
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessageResponseDto> authExceptionHandler(AuthException e) throws JsonProcessingException {

        log.error("Exception: AuthenticationException. " +
                "Exception message: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(getResponseBody(e.getMessage()));
    }


    @ExceptionHandler(OkException.class)
    public ResponseEntity<ErrorMessageResponseDto> okExceptionHandler(OkException e) throws JsonProcessingException{

        log.error("Exception: OkException. " +
                "Exception message: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getResponseBody(e.getMessage()));
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorMessageResponseDto> serverExceptionHandler(ServerException e) throws JsonProcessingException{

        log.error("Exception: ServerException. " +
                "Exception message: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getResponseBody(e.getMessage()));
    }


    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ErrorMessageResponseDto> validationExceptionHandler(
            ValidException e) throws JsonProcessingException{

        log.error("Exception: ValidException. " +
                "Exception message: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getResponseBody(e.getMessage()));
    }


    // VALIDATION - FOR FIELDS AND CLASSES VALIDATION
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorMessageResponseDto> validationExceptionHandler(
            MethodArgumentNotValidException e) throws JsonProcessingException {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
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

        log.error("Exception: MethodArgumentNotValidException. " +
                "Exception message: {}", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validErrorMessageResponseDto);
    }

}


