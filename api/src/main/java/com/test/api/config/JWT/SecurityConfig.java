package com.test.api.config.JWT;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@EnableAspectJAutoProxy
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            try{
                return http
                            .httpBasic(AbstractHttpConfigurer::disable)
                            .csrf(AbstractHttpConfigurer::disable)
                            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                            .authorizeHttpRequests(
                                    auth -> auth
                                            .requestMatchers(
                                                    "/user/auth/login", "/user/auth/newAccessToken",
                                                    "/ws/**", "/topic/**",
                                                    "/v3/api-docs/**", "/swagger-ui/**", "/v3/api-docs")
                                            .permitAll()
                                            .anyRequest().authenticated()
                            )
                            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                            .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                            .build();
            } catch (Exception e){
                    throw new AuthException("Authentication error:" + e.getMessage());
            }
        }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:63342")); // Frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Be careful with "*", restrict it to the necessary headers if possible
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

