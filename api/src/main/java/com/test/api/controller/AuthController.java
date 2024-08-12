package com.test.api.controller;

import com.test.api.JwtDomain.JwtRequest;
import com.test.api.JwtDomain.JwtResponse;
import com.test.api.JwtDomain.RefreshJwtRequest;
import com.test.api.service.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
