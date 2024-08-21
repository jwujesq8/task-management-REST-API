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
public class POSTUserRequestDto {

    @NotEmpty(message = "Login is required")
    @Size(
            min = 11,
            max = 50,
            message = "Min size is 11, max size is 50"
    )
    @Email(message = "Enter email address")
    private String login;

    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    @NotEmpty(message = "Password is required")
    private String password;

    @Size(
            max = 256,
            message = "Max size is 256"
    )
    @NotEmpty(message = "Full name is required")
    private String fullName;

    @Pattern(regexp = "^(male|female|none)$")
    private String genderName;

}
