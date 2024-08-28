package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorMessageResponseDto {

    @Schema(description = "Date and time", example = "08.08.08 15:18:13")
    private String dateTime;

    @Schema(description = "map of the field and errors description",
            example = "{\"id\": \"non valid id\"}")
    @NotNull(message = "Errors map must not be empty")
    private Map<String, String> errorsMap;
}
