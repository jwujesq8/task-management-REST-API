package com.test.api.controller;

import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.dto.request.RefreshJwtRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="Auth controller", description="Control the user authorization (JWT token)")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @Operation(summary = "log in")
    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful log in", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Unsuccessful log in", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))}
    )
    public ResponseEntity<JwtResponseDto> login(@Parameter(description = "user login and password inside the request body", required = true)
            @RequestBody @Valid @NotNull JwtRequestDto JwtRequestDto) throws ConstraintViolationException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.login(JwtRequestDto));
    }

    @Operation(summary = "get new access token (after that used refresh token will be non valid)")
    @PostMapping("/newAccessToken")
    @ResponseStatus(code = HttpStatus.OK)@ApiResponses({
            @ApiResponse(responseCode = "200", description = "You get a new access token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))}
    )
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authServiceImpl.getNewAccessToken(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @Operation(summary = "get new access and refresh token")
    @PostMapping("/refresh")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "JWT")@ApiResponses({
            @ApiResponse(responseCode = "200", description = "You get new access and refresh token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<JwtResponseDto> refresh(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.refresh(RefreshJwtRequestDto.getRefreshJwtRequest()));
    }

    @Operation(summary = "log out")
    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "JWT")
    @SecurityRequirement(name = "JWT")@ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful log out", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> logout(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        authServiceImpl.logout(RefreshJwtRequestDto.getRefreshJwtRequest());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponseDto("Successfully log out"));
    }

}
