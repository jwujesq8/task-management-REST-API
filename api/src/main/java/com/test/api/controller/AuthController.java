package com.test.api.controller;

import com.test.api.ApiApplication;
import com.test.api.dto.request.JwtRequestDto;
import com.test.api.dto.response.JwtResponseDto;
import com.test.api.dto.request.RefreshJwtRequestDto;
import com.test.api.dto.response.ErrorMessageResponseDto;
import com.test.api.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
@Validated
@RequiredArgsConstructor

@Tag(name="Auth controller", description="Control the user authorization (JWT token)")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "log in")
    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "Unsuccessful log in")}
    )
    public JwtResponseDto login(@Parameter(description = "user login and password inside the request body", required = true)
            @RequestBody @Valid @NotNull JwtRequestDto JwtRequestDto) throws ConstraintViolationException {
        logger.info(JwtRequestDto.getLogin() + " tries to log in");
        return authServiceImpl.login(JwtRequestDto);
    }

    @Operation(summary = "get new access token (after that used refresh token will be non valid)")
    @PostMapping("/newAccessToken")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json"))}
    )
    public JwtResponseDto getNewAccessToken(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        return authServiceImpl.getNewAccessToken(RefreshJwtRequestDto.getRefreshJwtRequest());
    }

    @Operation(summary = "get new access and refresh token")
    @PostMapping("/refresh")
    @SecurityRequirement(name = "JWT")@ApiResponses({
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json"))
    }
    )
    public JwtResponseDto refresh(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        return authServiceImpl.refresh(RefreshJwtRequestDto.getRefreshJwtRequest());
    }

    @Operation(summary = "log out")
    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = ErrorMessageResponseDto.class), mediaType = "application/json"))
    }
    )
    public void logout(@Parameter(description = "refresh user token inside the request body", required = true)
            @RequestBody @Valid @NotNull RefreshJwtRequestDto RefreshJwtRequestDto){
        authServiceImpl.logout(RefreshJwtRequestDto.getRefreshJwtRequest());
    }

}
