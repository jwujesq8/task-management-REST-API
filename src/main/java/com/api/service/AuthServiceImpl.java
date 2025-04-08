package com.api.service;

import com.api.config.JWT.JwtProvider;
import com.api.dto.jwt.JwtRequestDto;
import com.api.dto.jwt.JwtResponseDto;
import com.api.exception.AuthException;
import com.api.exception.BadRequestException;
import com.api.exception.OkException;
import com.api.entity.User;
import com.api.service.interfaces.UserService;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
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
    @Getter
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    public JwtResponseDto login(@Valid @NotNull JwtRequestDto jwtRequestDto) {

        final User user = userService.getUserByEmail(jwtRequestDto.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if(!refreshTokensStorage.containsKey(user.getEmail())){

            if(user.getPassword().equals(jwtRequestDto.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);

                refreshTokensStorage.put(user.getEmail(), refreshToken);
                log.info(user.getEmail() + " is logged in");
                return new JwtResponseDto(accessToken, refreshToken);
            }
            else {
                throw new AuthException("Wrong password");
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

                final User user = userService.getUserByEmail(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                refreshTokensStorage.put(user.getEmail(), null);

                log.info(user.getEmail() + " got new access token");
                return new JwtResponseDto(newAccessToken, null);
            }
            throw new AuthException("Wrong refresh token");
        }
        throw new AuthException("Non valid refresh token");

    }

    public JwtResponseDto refresh(@NotNull String refreshToken){

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByEmail(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getEmail(), newRefreshToken);

                log.info(user.getEmail() + " got new access token and refresh token");
                return new JwtResponseDto(newAccessToken, newRefreshToken);
            }
            throw new AuthException("Wrong refresh token");
        }
        throw new AuthException("Non valid refresh token");
    }

    public void logout(@NotNull String refreshToken) {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByEmail(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                refreshTokensStorage.remove(user.getEmail());

                log.info(user.getEmail() + " is logged out");
                return;
            }
            throw new OkException("User is already logged out");
        }
        throw new AuthException("Non valid refresh token");

    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


}
