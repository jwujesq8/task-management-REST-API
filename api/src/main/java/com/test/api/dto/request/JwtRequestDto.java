package com.test.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Schema(description = "user login", example = "iii@gmail.com")
    private String login;

    @NotEmpty(message = "Password is required")
    @Size(
            min = 7,
            max = 20,
            message = "Non valid password"
    )
    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    @Schema(description = "user password: digit 3 or more, special char 1 or more", example = "qqq_111")
    private String password;
}
