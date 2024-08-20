package com.test.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequestDto {
    @NotEmpty(message = "refresh token is required")
    private String refreshJwtRequest;
}
