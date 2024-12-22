package com.test.api.config.JWT;

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
                .setSubject(user.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
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
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    public boolean validateAccessToken(@NonNull String accessToken){
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken){
        return validateToken(refreshToken, refreshSecretKey);
    }

}

