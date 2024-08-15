package com.test.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}
