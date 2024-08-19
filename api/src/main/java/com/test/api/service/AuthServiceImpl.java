package com.test.api.service;

import com.test.api.config.JWT.JwtProvider;
import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.exception.ObjectNotFoundException;
import com.test.api.exception.UserAuthenticationException;
import com.test.api.user.User;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    public JwtResponseDto login(@Valid @NotNull JwtRequestDto JwtRequestDto) {

        final User user = userService.getUserByLogin(JwtRequestDto.getLogin())
                .orElseThrow(() -> new UserAuthenticationException("Unsuccessful authorization, user not found"));

        if(!refreshTokensStorage.containsKey(user.getLogin())){

            if(user.getPassword().equals(JwtRequestDto.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), refreshToken);
                return new JwtResponseDto(accessToken, refreshToken);
            }
            else {
                throw  new UserAuthenticationException("Unsuccessful authorization, wrong password");
            }
        } else {
            throw new UserAuthenticationException("User is already logged in");
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
            throw new UserAuthenticationException("Wrong refresh token");

        }
        throw new UserAuthenticationException("Non valid refresh token");
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
            throw new UserAuthenticationException("Wrong refresh token");
        }
        throw new UserAuthenticationException("Unsuccessful authorization, non valid refresh token");
    }

    public JwtResponseDto logout(@NotNull String refreshToken) {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new UserAuthenticationException("User not found"));

                refreshTokensStorage.remove(user.getLogin());

                return new JwtResponseDto(null, null);
            }
            throw new UserAuthenticationException("User is already logged out");
        }
        throw new UserAuthenticationException("Non valid refresh token");
    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


}
