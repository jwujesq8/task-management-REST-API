package com.test.api.service;

import com.test.api.JwtDomain.JwtAuthentication;
import com.test.api.JwtDomain.JwtRequest;
import com.test.api.JwtDomain.JwtResponse;
import com.test.api.user.User;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NotNull JwtRequest jwtRequest) throws AuthException {

        final User user = userService.getUserByLogin(jwtRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        if(user.getPassword().equals(jwtRequest.getPassword())){

            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshTokensStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        }
        else {
            throw  new AuthException("Wrong password");
        }
    }

    public JwtResponse getNewAccessToken(@NotNull String refreshToken) throws AuthException{

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);
            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateRefreshToken(user);
                return new JwtResponse(newAccessToken, null);
            }

        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NotNull String refreshToken) throws AuthException{
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

                return new JwtResponse(newAccessToken, newRefreshToken);
            }
        }
        throw new AuthException("Token is not valid");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
