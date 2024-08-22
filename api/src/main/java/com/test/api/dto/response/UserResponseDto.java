package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {

    @Schema(description = "user login", example = "iii@gmail.com")
    private String login;
    @Schema(description = "user full name", example = "Ole Szhaf")
    private String fullName;
    @Schema(description = "user gender: male, female or none", example = "female")
    private String gender;

}
