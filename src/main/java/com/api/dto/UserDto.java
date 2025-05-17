package com.api.dto;

import com.api.config.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

/**
 * Class UserDto
 *
 * Data Transfer Object (DTO) representing a user.
 * This DTO is used to transfer user data between the client and the server.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

        /**
         * The unique identifier of the user.
         * This is a UUID that uniquely identifies the user.
         */
        @NotNull(message = "Id must be")
        @Schema(description = "user id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        private UUID id;

        /**
         * The full name of the user.
         * The name must be between 1 and 256 characters long.
         */
        @NotNull(message = "Full name must be")
        @Size(
                min = 1,
                max = 256,
                message = "Full name acceptable  size 1-256"
        )
        @Schema(description = "User full name", example = "Name Surname")
        private String fullName;

        /**
         * The email address of the user.
         * This must be a valid email format.
         */
        @Email(message = "Non valid email")
        @NotNull(message = "Email must be")
        @Schema(description = "Users email", example = "iii@gmail.com")
        private String email;

        /**
         * The password for the user's account.
         * The password is required to be provided by the user.
         */
        @NotNull(message = "Password must be")
        @Schema(description = "Users password", example = "sdddd789")
        private String password;

        /**
         * The role of the user.
         * This can be one of the following values: "ADMIN" or "USER".
         */
        @Enumerated(EnumType.STRING)
        @Schema(description = "Users role: ADMIN|USER", example = "ADMIN")
        private Role role;
}
