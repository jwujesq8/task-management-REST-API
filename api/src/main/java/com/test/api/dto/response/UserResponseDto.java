package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {

    @Schema(description = "user id", example = "1")
    @Min(1L)
    @Max(Long.MAX_VALUE)
    private Long id;

    @Schema(description = "user login", example = "iii@gmail.com", minLength = 11, maxLength = 50)
    private String login;

    @Schema(description = "user full name", example = "Ole Szhaf", minLength = 1, maxLength = 256)
    private String fullName;

    @Schema(description = "user gender: male, female or none", example = "female", minLength = 4, maxLength = 6)
    private String genderName;

}
