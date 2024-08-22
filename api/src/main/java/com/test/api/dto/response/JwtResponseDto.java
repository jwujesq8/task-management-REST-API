package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {

    private final String type = "Bearer";

    @NotEmpty(message = "access token is required")
    @Schema(description = "access user token (received after successful login or refresh or get a new access token)")
    private String accessToken;

    @NotEmpty(message = "refresh token is required")
    @Schema(description = "refresh user token (received after successful login or refresh)")
    private String refreshToken;

}
