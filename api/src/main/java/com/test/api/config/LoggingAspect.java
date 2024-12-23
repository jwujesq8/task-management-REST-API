package com.test.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.controller.UserController;
import com.test.api.entity.UsersRequestsLogger;
import com.test.api.exception.ServerException;
import com.test.api.repository.UsersRequestsLoggerRepository;
import com.test.api.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;

@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private final UsersRequestsLoggerRepository usersRequestsLoggerRepository;
    private final HttpServletRequest request;
    private final UserService userService;
    private final UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_FIELD_LENGTH = 16777215;


    @AfterReturning(pointcut = "execution(* com.test.api.controller..*(..)) && "  +
            "!within(com.test.api.controller.AuthController)",
            returning = "response")
    public void logAfterRequest(JoinPoint joinPoint, Object response) throws IOException {
        saveToLogger(response);
    }


    public String setField(Object object) throws JsonProcessingException {
        if(object != null){
            String objectString = objectMapper.writeValueAsString(object);
            if (objectString.length() > MAX_FIELD_LENGTH) {
                objectString = objectString.substring(0, MAX_FIELD_LENGTH);
            }
            return objectString;
        }
        else return null;
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
                .requestBody(setField(UserController.getRequestBody()))
                .responseBody(setField(response))
                .build();


        usersRequestsLoggerRepository.save(usersRequestsLogger);
    }
}