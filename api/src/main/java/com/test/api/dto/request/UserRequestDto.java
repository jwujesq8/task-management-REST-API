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

    private Long id;

    @NotEmpty(message = "login is required")
    @Size(
            max = 50,
            min = 11,
            message = "Max size for login is 50"
    )
    private String login;

    @Pattern(regexp = "^(?=.*\\d{3,})(?=.*[^A-Za-z0-9])[\\S]{7,20}$", message = "Non valid password")
    @NotEmpty(message = "password is required")
    private String password;

    @Size(
            max = 256,
            message = "Max size for full name is 256"
    )
    @NotEmpty(message = "full name is required")
    private String fullName;

    private Gender gender;
}
