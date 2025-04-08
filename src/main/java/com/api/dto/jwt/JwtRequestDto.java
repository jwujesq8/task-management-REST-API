package com.api.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtRequestDto {

    @NotEmpty(message = "Email can't be empty or null")
    @Size(
            max = 50,
            min = 11,
            message = "Non valid login"
    )
    @Email(message = "Entered non email")
    @Schema(description = "user email", example = "iii@gmail.com", minLength = 11, maxLength = 50)
    private String email;

    @NotEmpty(message = "Password can't be empty or null")
    @Size(
            min = 7,
            max = 20,
            message = "Non valid password"
    )
//    @Pattern(regexp = "^(?=(?:.*\\d.*){3,})(?=.*[^A-Za-z0-9])[A-Za-z\\d[^A-Za-z0-9]]{7,20}$", message = "Non valid password (must be digit 3 or more, special char 1 or more, length 7-20)")
    @Schema(description = "user password", example = "qqq_111", minLength = 7, maxLength = 20)
    private String password;
}
