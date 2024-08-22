package com.test.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequestDto {
    @NotEmpty(message = "refresh token is required")
    @Schema(description = "refresh JWT token (used for getting a new login and refresh tokens)")
    private String refreshJwtRequest;
}
