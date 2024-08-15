package com.test.api.controller;

import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.dto.request.RefreshJwtRequestDto;
import com.test.api.service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto JwtRequestDto) throws AuthException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(JwtRequestDto));
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException{
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authService.getNewAccessToken(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JwtResponseDto> logout(@RequestBody RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.logout(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

}
