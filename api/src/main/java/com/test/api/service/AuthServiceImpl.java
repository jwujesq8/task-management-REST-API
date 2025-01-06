package com.test.api.service;

import com.test.api.config.JWT.JwtProvider;
import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.exception.AuthenticationException;
import com.test.api.exception.BadRequestException;
import com.test.api.exception.OkException;
import com.test.api.service.interfaces.UserService;
import com.test.api.user.User;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
public class AuthServiceImpl {

    private final UserService userService;
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    public JwtResponseDto login(@Valid @NotNull JwtRequestDto jwtRequestDto) {

        final User user = userService.getUserByLogin(jwtRequestDto.getLogin())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if(!refreshTokensStorage.containsKey(user.getLogin())){

            if(user.getPassword().equals(jwtRequestDto.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);

                refreshTokensStorage.put(user.getLogin(), refreshToken);
                log.info(user.getLogin() + " is logged in");
                return new JwtResponseDto(accessToken, refreshToken);
            }
            else {
                throw new AuthenticationException("Wrong password");
            }
        } else {
            throw new OkException("User is already logged in");
        }
    }

    public JwtResponseDto getNewAccessToken(@NotNull String refreshToken){

        if(jwtProvider.validateRefreshToken(refreshToken)){
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthenticationException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                refreshTokensStorage.put(user.getLogin(), null);

                log.info(user.getLogin() + " got new access token");
                return new JwtResponseDto(newAccessToken, null);
            }
            throw new AuthenticationException("Wrong refresh token");
        }
        throw new AuthenticationException("Non valid refresh token");

    }

    public JwtResponseDto refresh(@NotNull String refreshToken){

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthenticationException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), newRefreshToken);

                log.info(user.getLogin() + " got new access token and refresh token");
                return new JwtResponseDto(newAccessToken, newRefreshToken);
            }
            throw new AuthenticationException("Wrong refresh token");
        }
        throw new AuthenticationException("Non valid refresh token");
    }

    public void logout(@NotNull String refreshToken) {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthenticationException("User not found"));

                refreshTokensStorage.remove(user.getLogin());

                log.info(user.getLogin() + " is logged out");
                return;
            }
            throw new OkException("User is already logged out");
        }
        throw new AuthenticationException("Non valid refresh token");

    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


}
