package com.test.api.controller;

import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.dto.request.RefreshJwtRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid @NotNull JwtRequestDto JwtRequestDto) throws ConstraintViolationException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.login(JwtRequestDto));
    }

    @PostMapping("/newAccessToken")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authServiceImpl.getNewAccessToken(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.refresh(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<MessageResponseDto> logout(@RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        authServiceImpl.logout(RefreshJwtRequestDto.getRefreshJwtRequest());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponseDto("Successfully log out"));
    }

}
