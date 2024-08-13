package com.test.api.service;

import com.test.api.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Date expirationDate = new Date(currentDate.getTime() + 300000);

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
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, refreshSecretKey);
    }

}


// PREVIOUS FUNCTIONS

//private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
//    return Jwts.builder()
//            .setClaims(claims)
//            .setSubject(userDetails.getUsername())
//            .setIssuedAt(new Date(System.currentTimeMillis()))
//            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30))
//            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//            .compact();
//}
//
//private String generateToken(UserDetails userDetails){
//    return generateToken(new HashMap<>(), userDetails);
//}
//public Boolean isTokenValid(String token, String username) {
//    final String extractedUsername = extractUsername(token);
//    return (extractedUsername.equals(username) && !isTokenExpired(token));
//}
//
//private Boolean isTokenExpired(String token) {
//    return extractClaim(token, Claims::getExpiration).before(new Date());
//}
//
//public String extractUsername(String token) {
//    return extractClaim(token, Claims::getSubject);
//}
//
//
//
//private Key getSigningKey() {
//    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//    return Keys.hmacShaKeyFor(keyBytes);
//}
//
//private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//    final Claims claims = extractAllClaims(token);
//    return claimsResolver.apply(claims);
//}
