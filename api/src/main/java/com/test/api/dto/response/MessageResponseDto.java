package com.test.api.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    @NotBlank(message = "description is required")
    private String description;
}
