package com.test.api.config.JWT;

import com.test.api.exception.TokenValidationException;
import com.test.api.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;


    public JwtProvider(

            @Value("${jwt.secret.access}") String accessKey,
            @Value("${jwt.secret.refresh}") String refreshKey
    ) {
        accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));

//        log.info("accessToken: " + accessKey + ", refreshToken: " + refreshKey);
    }

    public String generateAccessToken(@NonNull User user) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));
        Date currentDate = calendar.getTime();
//        Date expirationDate = new Date(currentDate.getTime() + 300000); // 5 min
        Date expirationDate = new Date(currentDate.getTime() + 600000); // 10 min

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 5)) // around 5 min
                .setExpiration(expirationDate) // 5 min
                .claim("login", user.getLogin())
                .claim("fullName", user.getFullName())
                .signWith(accessSecretKey)
                .compact();
    }

    public String generateRefreshToken(@NotNull User user){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));
        Date currentDate = calendar.getTime();
        Date expirationDate = new Date(currentDate.getTime() + 86400000);

        return Jwts.builder()
                .setSubject(user.getLogin())
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 hours
                .setExpiration(expirationDate) // 24 hours
                .signWith(refreshSecretKey)
                .compact();
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecretKey);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, refreshSecretKey);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
//            throw new TokenValidationException("Token expired: " + expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
//            throw new TokenValidationException("Unsupported jwt: " + unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
//            throw new TokenValidationException("Malformed jwt: " + mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
//            throw new TokenValidationException("Invalid signature: " + sEx);
        } catch (Exception e) {
            log.error("Invalid token", e);
//            throw new TokenValidationException("Invalid token: " + e);
        }
        return false;
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken){
        return validateToken(refreshToken, refreshSecretKey);
    }

}

