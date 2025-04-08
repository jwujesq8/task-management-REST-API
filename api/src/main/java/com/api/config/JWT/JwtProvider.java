package com.api.config.JWT;

import com.api.entity.User;
import com.api.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            @Value("${jwt.access.path}") String accessPath,
            @Value("${jwt.refresh.path}") String refreshPath
    ) throws IOException {

        // ACCESS TOKEN
        Path a = Paths.get(accessPath).toAbsolutePath().normalize();
        if (Files.exists(a)) {
            String access = new String(Files.readAllBytes(a));
            accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(access));
        } else {
            throw new IOException("Access file not found: " + accessPath.toString());
        }

        // REFRESH TOKEN
        Path r = Paths.get(refreshPath).toAbsolutePath().normalize();
        if (Files.exists(r)) {
            String refresh = new String(Files.readAllBytes(r));
            refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refresh));
        } else {
            throw new IOException("Refresh file not found: " + accessPath.toString());
        }

    }

    public String generateAccessToken(@NonNull User user) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));
        Date currentDate = calendar.getTime();
        Date expirationDate = new Date(currentDate.getTime() + 600000); // 10 min

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .claim("login", user.getEmail())
                .claim("fullName", user.getFullName())
                .claim("role", user.getRole())
                .signWith(accessSecretKey)
                .compact();
    }

    public String generateRefreshToken(@NotNull User user){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));
        Date currentDate = calendar.getTime();
        Date expirationDate = new Date(currentDate.getTime() + 86400000);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expirationDate) // 24 hours
                .claim("role", user.getRole())
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
            log.error("Expired JWT token: {}", expEx.getMessage(), expEx);
            throw new AuthException("Token expired: " + expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported JWT token: {}", unsEx.getMessage(), unsEx);
            throw new AuthException("Unsupported JWT: " + unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed JWT token: {}", mjEx.getMessage(), mjEx);
            throw new AuthException("Malformed JWT: " + mjEx.getMessage());
        } catch (SignatureException sEx) {
            log.error("Invalid JWT signature: {}", sEx.getMessage(), sEx);
            throw new AuthException("Invalid signature: " + sEx.getMessage());
        } catch (Exception e) {
            log.error("Unknown JWT exception: {}", e.getMessage(), e);
            throw new AuthException("Auth exception: " + e.getMessage());
        }
    }

    public boolean validateAccessToken(@NonNull String accessToken){
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken){
        return validateToken(refreshToken, refreshSecretKey);
    }

}

