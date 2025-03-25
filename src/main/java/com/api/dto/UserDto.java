package com.api.dto;

import com.api.config.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

        @NotNull(message = "Id must be")
        private UUID id;

        @NotNull(message = "Full name nust be")
        private String fullName;

        @Email(message = "Non valid email")
        @NotNull(message = "Email must be")
        private String email;

        @NotNull(message = "Password must be")
        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;
}
