package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    @NotBlank(message = "description is required")
    @Schema(description = "response body message (error|success|created etc)", example = "Description about going well or wrong")
    private String description;
}
