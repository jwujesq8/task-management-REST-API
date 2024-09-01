package com.test.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.controller.UserController;
import com.test.api.entity.UsersRequestsLogger;
import com.test.api.exception.BadRequestException;
import com.test.api.exception.ServerException;
import com.test.api.repository.UsersRequestsLoggerRepository;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private final UsersRequestsLoggerRepository usersRequestsLoggerRepository;
    private final HttpServletRequest request;
    private final UserService userService;
    private final UserController userController;
    private ObjectMapper objectMapper = new ObjectMapper();


    @AfterReturning(pointcut = "execution(* com.test.api.controller..*(..)) && "  +
            "!within(com.test.api.controller.AuthController)",
            returning = "response")
    public void logAfterRequest(JoinPoint joinPoint, Object response) throws IOException {
        saveToLogger(response);
    }

    private void saveToLogger(Object response) throws IOException {

        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String method = request.getMethod();
        String requestUri = request.getRequestURI();

        UsersRequestsLogger usersRequestsLogger = UsersRequestsLogger.builder()
                .dateTimeUtc(Instant.now().atZone(ZoneId.of("UTC")).toInstant())
                .user(userService.getUserByLogin(user).orElseThrow(() -> new ServerException("Server error while reading logged in user")))
                .requestMethod(method)
                .requestPath(requestUri)
                .build();

        if (response != null){
            String responseString = objectMapper.writeValueAsString(response);
            if (responseString.length() > 16777215) {
                responseString = responseString.substring(0, 16777215);
            }
            usersRequestsLogger.setResponse(responseString);
        }
        if(userController.getRequestBody() != null){
            String requestBodyString = objectMapper.writeValueAsString(userController.getRequestBody());
            if (requestBodyString.length() > 16777215) {
                requestBodyString = requestBodyString.substring(0, 16777215);
            }
            usersRequestsLogger.setRequestBody(requestBodyString);
        }

        usersRequestsLoggerRepository.save(usersRequestsLogger);
    }
}