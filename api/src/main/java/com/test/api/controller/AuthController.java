package com.test.api.controller;

import com.test.api.entity.JwtRequest;
import com.test.api.entity.JwtResponse;
import com.test.api.entity.RefreshJwtRequest;
import com.test.api.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws AuthException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(jwtRequest));
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException{
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authService.getNewAccessToken(refreshJwtRequest.getRefreshJwtRequest()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(refreshJwtRequest.getRefreshJwtRequest()));
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JwtResponse> logout(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.logout(refreshJwtRequest.getRefreshJwtRequest()));
    }

}
