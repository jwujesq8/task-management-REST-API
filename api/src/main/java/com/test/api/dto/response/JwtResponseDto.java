package com.test.api.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {

    private final String type = "Bearer";

    @NotEmpty(message = "access token is required")
    private String accessToken;

    @NotEmpty(message = "refresh token is required")
    private String refreshToken;

}
