package com.test.api.controller;

import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.dto.request.RefreshJwtRequestDto;
import com.test.api.service.AuthServiceImpl;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid @NotNull JwtRequestDto JwtRequestDto) throws AuthException {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.login(JwtRequestDto));
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException{
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authServiceImpl.getNewAccessToken(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.refresh(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JwtResponseDto> logout(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto) throws AuthException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.logout(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

}
