package com.test.api.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestDto {

    @NotEmpty(message = "Login is required")
    @Size(
            max = 50,
            min = 11,
            message = "Non valid login"
    )
    @Email(message = "Enter email address")
    private String login;

    @NotEmpty(message = "Password is required")
    @Size(
            min = 7,
            max = 20,
            message = "Non valid password"
    )
    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    private String password;
}
