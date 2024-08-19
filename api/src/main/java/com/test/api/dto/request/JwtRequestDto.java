package com.test.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestDto {

    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
}
