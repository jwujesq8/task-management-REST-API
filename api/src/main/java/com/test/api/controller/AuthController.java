package com.test.api.controller;

import com.test.api.entity.JwtRequest;
import com.test.api.entity.JwtResponse;
import com.test.api.entity.RefreshJwtRequest;
import com.test.api.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws AuthException {
        final JwtResponse jwtResponse = authService.login(jwtRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException{
        final JwtResponse jwtResponse = authService.getNewAccessToken(refreshJwtRequest.getRefreshJwtRequest());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException {
        final JwtResponse jwtResponse = authService.refresh(refreshJwtRequest.getRefreshJwtRequest());
        return ResponseEntity.ok(jwtResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<JwtResponse> logout(@RequestBody RefreshJwtRequest refreshJwtRequest) throws AuthException{
        final JwtResponse jwtResponse = authService.logout(refreshJwtRequest.getRefreshJwtRequest());

        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
        ctxLogOut.logout((HttpServletRequest) refreshJwtRequest, (HttpServletResponse) jwtResponse, SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok(jwtResponse);
    }

}
