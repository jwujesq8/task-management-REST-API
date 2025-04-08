package com.api.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {

    @Schema(description = "authentication type", example = "Bearer", minLength = 6, maxLength = 6)
    private final String type = "Bearer";

    @NotEmpty(message = "Access token must not be empty or null")
    @Schema(description = "access user token (received after successful login or refresh or get a new access token)",
            example = "your.access.token")
    private String accessToken;

    @NotEmpty(message = "Refresh token must not be empty or null")
    @Schema(description = "refresh user token (received after successful login or refresh)",
            example = "your.refresh.token")
    private String refreshToken;

}
