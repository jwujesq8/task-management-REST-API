package com.test.api.service;

import com.test.api.entity.JwtAuthentication;
import com.test.api.entity.JwtRequest;
import com.test.api.entity.JwtResponse;
import com.test.api.entity.UserTokens;
import com.test.api.user.User;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final Map<String, String> refreshTokensStorage = new HashMap<>();
//    private final Map<String, UserToken> userTokensStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NotNull JwtRequest jwtRequest) throws AuthException {

        final User user = userService.getUserByLogin(jwtRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        if(!refreshTokensStorage.containsKey(user.getLogin())){

            if(user.getPassword().equals(jwtRequest.getPassword())){

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), refreshToken);
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
//                userTokensStorage.put(user.getLogin(), new JwtAuthentication(true, user.getLogin(),
//                        null, accessToken, refreshToken));

                return new JwtResponse(accessToken, refreshToken);
            }
            else {
                throw  new AuthException("Wrong password");
            }
        } else {
            throw new AuthException("user is already logged in");
        }


    }

    public JwtResponse getNewAccessToken(@NotNull String refreshToken) throws AuthException{

        if(jwtProvider.validateRefreshToken(refreshToken)){

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);
//            final String refreshTokenDB = userTokensStorage.get(login).getRefreshToken();

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
//                userTokensStorage.get(user.getLogin()).setAccessToken(newAccessToken);
//                userTokensStorage.get(user.getLogin()).setRefreshToken(null);
                refreshTokensStorage.put(user.getLogin(), null);
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
//            final String refreshTokenDB = userTokensStorage.get(login).getRefreshToken();

            if(refreshTokenDB!=null && refreshTokenDB.equals(refreshToken)){

                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokensStorage.put(user.getLogin(), newRefreshToken);
//                userTokensStorage.get(login).setAccessToken(newAccessToken);
//                userTokensStorage.get(login).setRefreshToken(refreshToken);

                return new JwtResponse(newAccessToken, newRefreshToken);
            }
        }
        throw new AuthException("Token is not valid");
    }

    public JwtResponse logout(@NotNull String refreshToken) throws AuthException {

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenDB = refreshTokensStorage.get(login);
//            final String refreshTokenDB = userTokensStorage.get(login).getRefreshToken();

            if (refreshTokenDB != null && refreshTokenDB.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));

//                userTokensStorage.remove(user.getLogin());
                refreshTokensStorage.remove(user.getLogin());

                Authentication userDetails = getAuthInfo();
                userDetails.setAuthenticated(false);

                // dont work
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                log.info("(authService) is authenticated : " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//                SecurityContextHolder.clearContext();

                return new JwtResponse(null, null);
            } else { throw new AuthException("user id already logged out");}
        }
        throw new AuthException("Token is not valid");
    }

    public boolean isUserLoggedIn(String login){
        return refreshTokensStorage.containsKey(login);
    }


    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
