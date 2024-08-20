package com.test.api.service;

import com.test.api.config.JWT.JwtProvider;
import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.exception.AlreadyLoggedInOrLoggedOutException;
import com.test.api.exception.ObjectNotFoundException;
import com.test.api.exception.TokenValidationException;
import com.test.api.exception.UserAuthenticationException;
import com.test.api.user.User;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserService userService;
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponseDto login(@Valid @NotNull JwtRequestDto jwtRequestDto) {

        final User user = userService.getUserByLogin(jwtRequestDto.getLogin())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if(!refreshTokensStorage.containsKey(user.getLogin())){

            if(user.getPassword().equals(jwtRequestDto.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), refreshToken);
                return new JwtResponseDto(accessToken, refreshToken);
            }
            else {
                throw new UserAuthenticationException("Wrong password");
            }
        } else {
            throw new AlreadyLoggedInOrLoggedOutException("User is already logged in");
        }


    }

    public JwtResponseDto getNewAccessToken(@NotNull String refreshToken){

        if(jwtProvider.validateRefreshToken(refreshToken)){
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new UserAuthenticationException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                refreshTokensStorage.put(user.getLogin(), null);
                return new JwtResponseDto(newAccessToken, null);
            }
            throw new TokenValidationException("Wrong refresh token");
        }
        throw new TokenValidationException("Non valid refresh token");

    }

    public JwtResponseDto refresh(@NotNull String refreshToken){

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new UserAuthenticationException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), newRefreshToken);

                return new JwtResponseDto(newAccessToken, newRefreshToken);
            }
            throw new TokenValidationException("Wrong refresh token");
        }
        throw new TokenValidationException("Non valid refresh token");
    }

    public void logout(@NotNull String refreshToken) {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new UserAuthenticationException("User not found"));

                refreshTokensStorage.remove(user.getLogin());
                return;
            }
            throw new AlreadyLoggedInOrLoggedOutException("User is already logged out");
        }
        throw new UserAuthenticationException("Non valid refresh token");

    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


}
