package com.test.api.dto.request;

import com.test.api.user.Gender;
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

    @NotNull(message = "Id is required")
    private Long id;

    @NotEmpty(message = "Login is required")
    @Size(
            min = 11,
            max = 50,
            message = "Min size is 11, max size is 50"
    )
    private String login;

    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    @NotEmpty(message = "Password is required")
    private String password;

    @Size(
            max = 256,
            message = "Non valid full name"
    )
    @NotEmpty(message = "Full name is required")
    private String fullName;

    @Pattern(regexp = "^(male|female|none)$")
    private Gender gender;
}
