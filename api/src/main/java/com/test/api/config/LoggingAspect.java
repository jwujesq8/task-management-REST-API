package com.test.api.config;

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
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private final UsersRequestsLoggerRepository usersRequestsLoggerRepository;
    private final HttpServletRequest request;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final UserService userService;


    @AfterReturning(pointcut = "execution(* com.test.api.controller..*(..)) && " +
            "!execution(* com.test.api.controller.AuthController.login(..)) && " +
            "!execution(* com.test.api.controller.AuthController.newAccessToken(..))",
            returning = "response")
    public void logAfterRequest(JoinPoint joinPoint, Object response) {
        saveToLogger(response);
    }

    private void saveToLogger(Object response) {

        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        UsersRequestsLogger usersRequestsLogger = UsersRequestsLogger.builder()
                .dateTimeUtc(Instant.now().atZone(ZoneId.of("UTC")).toInstant())
                .user(userService.getUserByLogin(user).orElseThrow(() -> new ServerException("Server error while reading logged in user")))
                .requestMethod(method)
                .requestPath(requestUri)
                .response(String.valueOf(response.getClass()).replace("class ", ""))
                .build();

        usersRequestsLoggerRepository.save(usersRequestsLogger);
    }
}
