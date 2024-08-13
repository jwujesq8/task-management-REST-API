package com.test.api.filter;

import com.test.api.entity.JwtAuthentication;
import com.test.api.service.AuthService;
import com.test.api.service.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }

        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);

            final JwtAuthentication jwtAuthentication = new JwtAuthentication();
            jwtAuthentication.setLogin(claims.getSubject());
            jwtAuthentication.setAuthenticated(authService.isUserLoggedIn(claims.getSubject()));
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

        }
        filterChain.doFilter(request, response);
    }

}

