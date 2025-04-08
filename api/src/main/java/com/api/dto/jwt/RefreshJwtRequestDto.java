package com.api.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequestDto {

    @NotEmpty(message = "Refresh token must not be empty or null")
    @Schema(description = "refresh JWT token (used for getting a new login and refresh tokens)", example = "your.refresh.token")
    private String refreshJwtRequest;

}
