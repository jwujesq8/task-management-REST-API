package com.test.api.dto.request;

import com.test.api.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class POSTUserRequestDto {

    @NotEmpty(message = "Login is required")
    @Size(
            min = 11,
            max = 50,
            message = "Min size is 11, max size is 50"
    )
    @Email(message = "Enter email address")
    @Schema(description = "user login", example = "iii@gmail.com", minLength = 11, maxLength = 50)
    private String login;

    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    @NotEmpty(message = "Password is required")
    @Schema(description = "user password: digit 3 or more, special char 1 or more", example = "qqq_111", minLength = 7, maxLength = 20)
    private String password;

    @Size(
            max = 256,
            min = 1,
            message = "Max size is 256"
    )
    @NotEmpty(message = "Full name is required")
    @Schema(description = "user full name", example = "Ole Szhaf", minLength = 1, maxLength = 256)
    private String fullName;

    @Pattern(regexp = "^(male|female|none)$", message = "may be only male, female or none")
    @Schema(description = "user gender: male, female or none", example = "female", minLength = 4, maxLength = 6)
    @NotNull(message = "must be male, female or none")
    private String genderName;

}
