package com.api.dto;

import com.api.config.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

        @NotNull(message = "Id must be")
        @Schema(description = "user id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        private UUID id;

        @NotNull(message = "Full name must be")
        @Size(
                min = 1,
                max = 256,
                message = "Full name acceptable  size 1-256"
        )
        @Schema(description = "User full name", example = "Name Surname")
        private String fullName;

        @Email(message = "Non valid email")
        @NotNull(message = "Email must be")
        @Schema(description = "Users email", example = "iii@gmail.com")
        private String email;

        @NotNull(message = "Password must be")
        @Schema(description = "Users password", example = "sdddd789")
        private String password;

        @Enumerated(EnumType.STRING)
        @Schema(description = "Users role: ADMIN|USER", example = "ADMIN")
        private Role role;
}
