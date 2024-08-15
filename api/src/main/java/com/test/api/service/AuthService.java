package com.test.api.service;

import com.test.api.config.JWT.JwtProvider;
import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.user.User;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponseDto login(@NotNull JwtRequestDto JwtRequestDto) throws AuthException {

        final User user = userService.getUserByLogin(JwtRequestDto.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        if(!refreshTokensStorage.containsKey(user.getLogin())){

            if(user.getPassword().equals(JwtRequestDto.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), refreshToken);
                return new JwtResponseDto(accessToken, refreshToken);
            }
            else {
                throw  new AuthException("Wrong password");
            }
        } else {
            throw new AuthException("User is already logged in");
        }


    }

    public JwtResponseDto getNewAccessToken(@NotNull String refreshToken) throws AuthException{

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                refreshTokensStorage.put(user.getLogin(), null);
                return new JwtResponseDto(newAccessToken, null);
            }
            throw new AuthException("Wrong refresh token");

        }
        throw new AuthException("Non valid refresh token");
    }

    public JwtResponseDto refresh(@NotNull String refreshToken) throws AuthException{

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), newRefreshToken);

                return new JwtResponseDto(newAccessToken, newRefreshToken);
            }
            throw new AuthException("Wrong refresh token");
        }
        throw new AuthException("Non valid refresh token");
    }

    public JwtResponseDto logout(@NotNull String refreshToken) throws AuthException {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                refreshTokensStorage.remove(user.getLogin());

                return new JwtResponseDto(null, null);
            }
            throw new AuthException("User is already logged out");
        }
        throw new AuthException("Non valid refresh token");
    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


}
