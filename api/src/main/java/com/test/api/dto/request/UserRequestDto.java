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
public class UserRequestDto {

    @NotNull(message = "Id must not be null")
    @Min(1L)
    @Max(Long.MAX_VALUE)
    private Long id;

    @NotEmpty(message = "Login must not be empty or null")
    @Size(
            min = 11,
            max = 50,
            message = "Min size is 11, max size is 50"
    )
    @Schema(description = "user login", minLength = 11, maxLength = 50)
    private String login;

    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$",
            message = "Non valid password (must be digit 3 or more, special char 1 or more, length 7-20)")
    @NotEmpty(message = "Password must not be empty or null")
    @Schema(description = "user password: digit 3 or more, special char 1 or more, length 7-20", minLength = 7, maxLength = 20)
    private String password;

    @Size(
            max = 256,
            min = 1,
            message = "Non valid full name"
    )
    @NotEmpty(message = "Full name must not be empty or null")
    @Schema(description = "user full name", minLength = 1, maxLength = 256)
    private String fullName;

    @Pattern(regexp = "^(male|female|none)$")
    @Schema(description = "user gender", minLength = 4, maxLength = 6)
    @NotNull(message = "Gender name must be only male, female or none")
    private Gender genderName;
}
